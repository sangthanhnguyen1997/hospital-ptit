//package ptit.hospitalmanagementsystem.controller;
//
//@RestController
//@RequestMapping("/patient")
//@RequiredArgsConstructor
//public class PatientController {
//
//    private final PatientService patientService;
//
//    // POST /patient/list?page=0&size=10&sort=createdAt,desc
//    @PostMapping("/list")
//    public ResponseEntity<ListResponse<PatientResponse>> getPatients(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt,desc") String sort,
//            @RequestBody PatientSearchRequest searchRequest) { // Chứa name, phoneNumber
//        return ResponseEntity.ok(patientService.getPatients(page, size, sort, searchRequest));
//    }
//
//    // GET /patient/get/{id}
//    @GetMapping("/get/{id}")
//    public ResponseEntity<DetailResponse<PatientResponse>> getPatientById(@PathVariable Integer id) {
//        return ResponseEntity.ok(patientService.getPatientById(id));
//    }
//
//    // POST /patient/post
//    @PostMapping("/post")
//    public ResponseEntity<SuccessResponse> createPatient(@RequestBody PatientCreateRequest request) {
//        patientService.createPatient(request);
//        return ResponseEntity.ok(new SuccessResponse("Patient created successfully"));
//    }
//
//    // PUT /patient/put/{id}
//    @PutMapping("/put/{id}")
//    public ResponseEntity<SuccessResponse> updatePatient(
//            @PathVariable Integer id,
//            @RequestBody PatientUpdateRequest request) {
//        patientService.updatePatient(id, request);
//        return ResponseEntity.ok(new SuccessResponse("Patient updated successfully"));
//    }
//
//    // DELETE /patient/delete
//    @DeleteMapping("/delete")
//    public ResponseEntity<SuccessResponse> deletePatients(@RequestBody DeleteRequest request) { // Chứa list ids
//        patientService.deletePatients(request.getIds());
//        return ResponseEntity.ok(new SuccessResponse("Patients deleted successfully"));
//    }
//}