//package ptit.hospitalmanagementsystem.controller;
//
//@RestController
//@RequestMapping("/clinic")
//@RequiredArgsConstructor
//public class ClinicController {
//
//    private final ClinicService clinicService;
//
//    // POST /clinic/list?page=0&size=10
//    @PostMapping("/list")
//    public ResponseEntity<ListResponse<ClinicResponse>> getClinics(
//
//    }
//
//    // GET /clinic/get/{id}
//    @GetMapping("/get/{id}")
//    public ResponseEntity<DetailResponse<ClinicResponse>> getClinicById(@PathVariable Integer id) {
//        return ResponseEntity.ok(clinicService.getClinicById(id));
//    }
//
//    // POST /clinic/post
//    @PostMapping("/post")
//    public ResponseEntity<SuccessResponse> createClinic(@RequestBody ClinicCreateRequest request) {
//
//        return ResponseEntity.ok(new SuccessResponse("Clinic created successfully"));
//    }
//
//    // PUT /clinic/put/{id}
//    @PutMapping("/put/{id}")
//    public ResponseEntity<SuccessResponse> updateClinic(
//
//        return ResponseEntity.ok(new SuccessResponse("Clinic updated successfully"));
//    }
//
//    // DELETE /clinic/delete
//    @DeleteMapping("/delete")
//    public ResponseEntity<SuccessResponse> deleteClinics(@RequestBody DeleteRequest request) {
//
//        return ResponseEntity.ok(new SuccessResponse("Clinics deleted successfully"));
//    }
//}