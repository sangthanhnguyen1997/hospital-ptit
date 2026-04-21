package ptit.hospitalmanagementsystem.service;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ptit.hospitalmanagementsystem.dto.request.AuthenticationRequest;
import ptit.hospitalmanagementsystem.dto.request.IntrospectRequest;
import ptit.hospitalmanagementsystem.dto.request.LogoutRequest;
import ptit.hospitalmanagementsystem.dto.request.RefreshRequest;
import ptit.hospitalmanagementsystem.dto.respond.AuthenticationResponse;
import ptit.hospitalmanagementsystem.dto.respond.IntrospectResponse;
import ptit.hospitalmanagementsystem.entity.InvalidatedToken;
import ptit.hospitalmanagementsystem.entity.User;
import ptit.hospitalmanagementsystem.exception.AppException;
import ptit.hospitalmanagementsystem.exception.ErrorCode;
import ptit.hospitalmanagementsystem.repository.UserRepository;
import ptit.hospitalmanagementsystem.repository.InvalidatedTokenRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    /**
     * HÀM ĐĂNG NHẬP (AUTHENTICATE)
     * Mục tiêu: Kiểm tra user/pass -> Nếu đúng thì cấp "thẻ bài" (Token).
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        // 1. Tìm user trong DB theo username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. So sánh mật khẩu (Mật khẩu gửi lên vs Mật khẩu băm trong DB)
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // 3. Đúng pass thì tạo Token
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    /**
     * HÀM KIỂM TRA THẺ BÀI (INTROSPECT)
     * Mục tiêu: Một Token gửi lên có còn "xịn" không? Có bị sửa hay hết hạn không?
     */
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token,false);
        }catch (AppException e){
            isValid = false;
        }

        // Token hợp lệ KHI VÀ CHỈ KHI: Chữ ký đúng VÀ thời gian hết hạn phải sau thời điểm hiện tại
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    //logout token
    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
            log.error("Token already expired");
        }

    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {


        // 1. Tạo bộ xác thực (Verifier) sử dụng cái chìa khóa bí mật (SIGNER_KEY)
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // 2. "Mổ xẻ" chuỗi Token String thành đối tượng SignedJWT để đọc thông tin
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 3. Kiểm tra xem Token đã hết hạn chưa?
        Date expiryTime =(isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();

        // 4. Dùng verifier để kiểm tra chữ ký (Signature) có khớp với Header+Payload không
        var verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        //kiem tra xem token id co trong database quan ly logout khong?
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    //refresh lai token tra cho frontend
    public AuthenticationResponse refreshToken(RefreshRequest request)throws JOSEException, ParseException {
        var signJWT = verifyToken(request.getToken(),true);
        //invalidate token cu
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    /**
     * HÀM TẠO THẺ BÀI (GENERATE TOKEN)
     * Quy trình: Header + Payload --(SIGNER_KEY)--> Signature
     */
    private String generateToken(User user){
        // BƯỚC 1: TẠO HEADER (Phần đầu của Token)
        // Khai báo thuật toán mã hóa sẽ dùng, ở đây là HS512 (HmacSHA512)
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // BƯỚC 2: TẠO PAYLOAD - CHỨA CÁC THÔNG TIN (CLAIMS) CỦA TOKEN
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // Định danh người dùng (thường là username)
                .issuer("ptit.hospital")     // Định danh đơn vị cấp phát Token (Bệnh viện PTIT)
                .issueTime(new Date())       // Thời điểm phát hành Token (bây giờ)

                // Thiết lập thời điểm hết hạn = Thời điểm hiện tại + Số giây cấu hình
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))

                // Cấp một ID duy nhất cho Token (JIT) để phục vụ tính năng Logout/Invalidate sau này
                .jwtID(UUID.randomUUID().toString())

                // Gắn quyền hạn của User (Role) vào claim "scope" để Spring Security phân quyền
                .claim("scope", buildScope(user))
                .build();

        // BƯỚC 3: ĐÓNG GÓI PAYLOAD VÀO ĐỐI TƯỢNG JWS
        // Chuyển đổi nội dung Claims sang định dạng JSON
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // BƯỚC 4: KÝ TÊN (SIGNING)
            // Dùng SIGNER_KEY (khóa bí mật) để "niêm phong" Token, đảm bảo dữ liệu không bị sửa đổi
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // BƯỚC 5: SERIALIZE (Chuyển đổi thành chuỗi String)
            // Biến đối tượng thành chuỗi dạng xxxxx.yyyyy.zzzzz (Base64) để gửi về cho Frontend
            return jwsObject.serialize();

        } catch (JOSEException e) {
            // Ghi log nếu có lỗi trong quá trình ký tên (ví dụ: Key quá ngắn)
            log.error("Cannot create token for user: {}", e);
            throw new RuntimeException("cannot create token");
        }
    }
    private String buildScope(User user) {
        if (user.getRoles() != null) {
            // Vì chỉ có 1 Role, chúng ta chỉ cần trả về tên Role kèm tiền tố ROLE_
            // Ví dụ: "ROLE_ADMIN" hoặc "ROLE_BACSI"
            return "ROLE_" + user.getRoles().name();
        }
        return ""; // Trả về chuỗi rỗng nếu không có role
    }
}
