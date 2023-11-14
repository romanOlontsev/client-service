package ru.neoflex.users.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.users.exceptions.BadRequestException;
import ru.neoflex.users.exceptions.DataAlreadyExistsException;
import ru.neoflex.users.exceptions.DataNotFoundException;
import ru.neoflex.users.mappers.HeaderValidationMapper;
import ru.neoflex.users.models.entities.Account;
import ru.neoflex.users.models.entities.HeaderValidation;
import ru.neoflex.users.models.requests.HeaderValidationRequest;
import ru.neoflex.users.models.responses.HeaderValidationResponse;
import ru.neoflex.users.repositories.HeaderValidationRepository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeaderValidationService {

    private final HeaderValidationRepository repository;

    private final HeaderValidationMapper mapper;

    public HeaderValidation getHeaderValidationByHeader(String header) {
        return repository.findHeaderValidationByHeader(header)
                         .orElseThrow(() -> new DataNotFoundException(String.format("Header: %s not supported", header)));
    }

    public List<HeaderValidationResponse> getHeaderValidationRules() {
        List<HeaderValidation> rules = repository.findAllHeaderValidation();
        log.info("Rules={} have been requested", rules);
        return mapper.headerValidationResponseListFromHeaderValidationList(rules);
    }

    public void addHeaderValidation(HeaderValidationRequest request) {
        checkHeaderForExistence(request);
        List<String> requiredFieldsRequest = removeDuplicates(request);
        checkAccountFieldsForExistence(requiredFieldsRequest);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(request.getHeaderName())
                                                            .requiredFields(requiredFieldsRequest)
                                                            .build();
        HeaderValidation savedRule = repository.save(headerValidation);
        log.info("HeaderValidation={} has been saved", savedRule);
    }

    @Transactional
    public void updateHeaderValidation(HeaderValidationRequest request) {
        HeaderValidation foundHeaderValidation = getHeaderValidationByHeader(request.getHeaderName());
        List<String> requiredFields = request.getRequiredFields();
        checkAccountFieldsForExistence(requiredFields);
        foundHeaderValidation.setRequiredFields(requiredFields);
        log.info("HeaderValidation={} has been updated", foundHeaderValidation);
    }

    @Transactional
    public void deleteHeaderValidation(String header) {
        HeaderValidation foundHeaderValidation = getHeaderValidationByHeader(header);
        repository.deleteByHeader(header);
        log.info("HeaderValidation={} has been deleted", foundHeaderValidation);
    }

    private void checkHeaderForExistence(HeaderValidationRequest request) {
        String headerName = request.getHeaderName();
        repository.findHeaderValidationByHeader(headerName)
                  .ifPresent(it -> {
                      throw new DataAlreadyExistsException(String.format("Header %s already exists", headerName));
                  });
    }

    private List<String> removeDuplicates(HeaderValidationRequest request) {
        return request.getRequiredFields()
                      .stream()
                      .distinct()
                      .toList();
    }

    private void
    checkAccountFieldsForExistence(List<String> requiredFieldsRequest) {
        Set<String> accountFieldList = Arrays.stream(Account.class.getDeclaredFields())
                                             .map(Field::getName)
                                             .filter(it -> !it.equals("id"))
                                             .collect(Collectors.toSet());

        if (!accountFieldList.containsAll(requiredFieldsRequest)) {
            throw new BadRequestException("Request contains unknown fields for validation");
        }
    }
}
