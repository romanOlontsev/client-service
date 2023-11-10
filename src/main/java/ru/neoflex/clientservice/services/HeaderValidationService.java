package ru.neoflex.clientservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.clientservice.exceptions.DataAlreadyExistsException;
import ru.neoflex.clientservice.mappers.HeaderValidationMapper;
import ru.neoflex.clientservice.models.entities.HeaderValidation;
import ru.neoflex.clientservice.models.requests.HeaderValidationRequest;
import ru.neoflex.clientservice.repositories.HeaderValidationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeaderValidationService {

    private final HeaderValidationRepository repository;

    private final HeaderValidationMapper mapper;

    public List<String> getRequiredFieldsByHeader(String header) {
        return repository.findRequiredFieldsByHeader(header);
    }

    //  TODO
//    check fields of Account class
    public void addHeaderValidation(HeaderValidationRequest request) {
        String headerName = request.getHeaderName();
        repository.findHeaderIdByName(headerName)
                  .ifPresent(it -> {
                      throw new DataAlreadyExistsException(String.format("Header %s already exists", headerName));
                  });

        HeaderValidation headerValidation = mapper.headerValidationFromHeaderValidationRequest(request);
        repository.save(headerValidation);
    }
}
