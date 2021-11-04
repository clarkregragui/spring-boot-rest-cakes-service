package com.riggy.example.cakes.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Files;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riggy.example.cakes.model.dto.CakeDTO;
import com.riggy.example.cakes.service.CakeService;



@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CakeAppTest {
	
	
    @Autowired
    private TestRestTemplate restTemplate;

    
    @SpyBean
    private CakeService cakeService;
        
    private static String expectedCakesView;
    
    private static List<CakeDTO> expectedCakesList;
    
    @BeforeAll
    public static void beforeClass() throws IOException {
    	Resource cakesViewFileResource = new ClassPathResource("expectedCakesView.html");
    	expectedCakesView = Files.contentOf(cakesViewFileResource.getFile(), StandardCharsets.UTF_8);
    	expectedCakesView.trim();
    	
    	Resource cakesJsonFileResource = new ClassPathResource("cakes.json");
    	String json = Files.contentOf(cakesJsonFileResource.getFile(), StandardCharsets.UTF_8);
    	ObjectMapper mapper = new ObjectMapper();
    	expectedCakesList = mapper.readValue(json, new TypeReference<List<CakeDTO>>(){});
    	

    }
    
    
    @Test
    public void getCakesTest() {
        ResponseEntity<List<CakeDTO>> response = restTemplate.exchange("/cakes",
            HttpMethod.GET, null, new ParameterizedTypeReference<List<CakeDTO>>() {
            });
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CakeDTO> cakes = response.getBody();
        assertThat(cakes).hasSize(5);
        assertThat(cakes).containsAll(expectedCakesList);
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void postCreateCakeTest() {

    	CakeDTO newCake = new CakeDTO("Chocolate cake", "Very chocolatey",
    			"chocolate_cake_img.jpeg",Lists.list("flour", "eggs", "milk", "suger")
    			, new BigDecimal("12.99"));


    	HttpHeaders headers = new HttpHeaders();

    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    	HttpEntity<CakeDTO> request = new HttpEntity<CakeDTO>(newCake, headers);

    	ResponseEntity<CakeDTO> postResponse = restTemplate.exchange("/cakes",
    			HttpMethod.POST, request, new ParameterizedTypeReference<CakeDTO>() {
    	});
    	
    	assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    	
    	
    	ResponseEntity<List<CakeDTO>> getResponse = restTemplate.exchange("/cakes",
    			HttpMethod.GET, null, new ParameterizedTypeReference<List<CakeDTO>>() {
    	});

    	assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	List<CakeDTO> cakes = getResponse.getBody();
    	assertThat(cakes).hasSize(6);
    	assertThat(cakes).contains(newCake);
    	
    }
    
    @Test
    public void getCakesViewTest() {
    	
    	ResponseEntity<String> getResponse = restTemplate.exchange("/",
    			HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
    	});
    	
    	assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(getResponse.getBody().trim()).isEqualTo(expectedCakesView.trim());
    	
    }
    
    /* Error cases*/
    @Test
    public void postBadRequestUniqueConstraintViolationTest() {
    	
    	CakeDTO existingCake = new CakeDTO("Lemon cheesecake", "A cheesecake made of lemon",
    			"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"
    			,Lists.list("flour", "eggs", "milk", "suger")
    			, new BigDecimal("12.99"));

    	ResponseEntity<String> postResponse = restTemplate.postForEntity("/cakes", existingCake, String.class);
    	
    	assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    	assertThat(postResponse.getBody()).contains("Integrity constraint violated");
    }
    
    @Test
    public void postBadRequestMissingFieldsTest() {
    	
    	CakeDTO existingCake = new CakeDTO(null, "A cheesecake made of lemon",
    			"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"
    			,Lists.list("flour", "eggs", "milk", "suger")
    			, new BigDecimal("12.99"));
    	
    	ResponseEntity<String> postResponse = restTemplate.postForEntity("/cakes", existingCake, String.class);
    	
    	assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    	assertThat(postResponse.getBody()).contains("A title is mandatory");
    }
    
    @Test
    public void postBadRequestInvalidJsonTest() {
    	
    	//json missing comma
    	String invalidCakeJson = "{\n"
    			+ "  \"title\" : \"Lemon cheesecake\"\n"
    			+ "  \"desc\" : \"A cheesecake made of lemon\",\n"
    			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
    			+ "}";
    	
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add("Content-Type", "application/json");
    	HttpEntity<String> httpEntity = new HttpEntity<>(invalidCakeJson, headers);

    	ResponseEntity<String> postResponse = restTemplate.postForEntity("/cakes", httpEntity, String.class);
    	
    	assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void getServerError500Test() {
    	
    	doThrow(RuntimeException.class).when(cakeService).findCakes();
    	
        ResponseEntity<String> errorResponse = restTemplate.exchange("/cakes",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                });
        verify(cakeService, times(1)).findCakes();
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
