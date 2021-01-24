package org.example.controllers;


import org.junit.jupiter.api.DisplayName;;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;



@DisplayName("Start DepartmentUIController testing")
public class DepartmentUIControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DepartmentsUIController controller ;

}
