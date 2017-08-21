package org.shanoir.ng.subject;

import java.util.List;

import org.shanoir.ng.shared.exception.RestServiceException;
import org.shanoir.ng.subject.dto.SubjectStudyCardIdDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "subject", description = "the subject API")
public interface SubjectApi {

	@ApiOperation(value = "", notes = "Deletes a subject", response = Void.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "subject deleted", response = Void.class),
			@ApiResponse(code = 204, message = "no subject found", response = Void.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Void.class),
			@ApiResponse(code = 403, message = "forbidden", response = Void.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Void.class) })
	@RequestMapping(value = "/subject/{subjectId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	ResponseEntity<Void> deleteSubject(
			@ApiParam(value = "id of the subject", required = true) @PathVariable("subjectId") Long subjectId);

	@ApiOperation(value = "", notes = "Returns all the subjects", response = Subject.class, responseContainer = "List", tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found subjects", response = Subject.class),
			@ApiResponse(code = 204, message = "no subject found", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject/all", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Subject>> findSubjects();

	@ApiOperation(value = "", notes = "If exists, returns the subject corresponding to the given id", response = Subject.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found bubject", response = Subject.class),
			@ApiResponse(code = 204, message = "no subject found", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject/{subjectId}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<Subject> findSubjectById(
			@ApiParam(value = "id of the subject", required = true) @PathVariable("subjectId") Long subjectId);

	@ApiOperation(value = "", notes = "Saves a new subject", response = Subject.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "created subject", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 422, message = "bad parameters", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Subject> saveNewSubject(
			@ApiParam(value = "subject to create", required = true) @RequestBody Subject subject,
			final BindingResult result) throws RestServiceException;

	@ApiOperation(value = "", notes = "Saves a new subject with auto generated common name", response = Subject.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "created subject", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 422, message = "bad parameters", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject/OFSEP/", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Subject> saveNewOFSEPSubject(
			@ApiParam(value = "subject to create and the id of the study card", required = true) @RequestBody SubjectStudyCardIdDTO subjectStudyCardIdDTO,
			final BindingResult result) throws RestServiceException;

	@ApiOperation(value = "", notes = "Updates a subject", response = Void.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 204, message = "subject updated", response = Void.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Void.class),
			@ApiResponse(code = 403, message = "forbidden", response = Void.class),
			@ApiResponse(code = 422, message = "bad parameters", response = Void.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Void.class) })
	@RequestMapping(value = "/subject/{subjectId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Void> updateSubject(
			@ApiParam(value = "id of the subject", required = true) @PathVariable("subjectId") Long subjectId,
			@ApiParam(value = "subject to update", required = true) @RequestBody Subject subject,
			final BindingResult result) throws RestServiceException;

	@ApiOperation(value = "", notes = "If exists, returns the subjects of a study", response = Subject.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found subjects", response = Subject.class),
			@ApiResponse(code = 204, message = "no subject found", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject/{studyId}/allSubjects", produces = {
			"application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Subject>> findSubjectsByStudyId(
			@ApiParam(value = "id of the study", required = true) @PathVariable("studyId") Long studyId);

	@ApiOperation(value = "", notes = "If exists, returns the subject corresponding to the given identifier", response = Subject.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found subject", response = Subject.class),
			@ApiResponse(code = 204, message = "no subject found", response = Subject.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Subject.class),
			@ApiResponse(code = 403, message = "forbidden", response = Subject.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Subject.class) })
	@RequestMapping(value = "/subject/findByIdentifier/{subjectIdentifier}", produces = {
			"application/json" }, method = RequestMethod.GET)
	ResponseEntity<Subject> findSubjectByIdentifier(
			@ApiParam(value = "identifier of the subject", required = true) @PathVariable("subjectIdentifier") String subjectIdentifier);

}