package com.yalov4uk.interfaces;

import com.yalov4uk.dto.StatementDto;
import com.yalov4uk.interfaces.abstracts.IBaseService;

import java.util.List;

public interface IAdminService extends IBaseService {

    StatementDto registerStatement(Integer userId, Integer facultyId);

    List<StatementDto> calculateEntrants(Integer facultyId);

    List<String> calculateEntrantsBeautifulOutput(Integer facultyId);
}
