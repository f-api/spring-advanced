package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherClient weatherClient;

    @Test
    void 날씨_API_응답이_정상적이지_않으면_예외가_발생한다() {
        // Given
        ResponseEntity<WeatherDto[]> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WeatherDto[].class)))
                .thenReturn(responseEntity);

        // When & Then
        assertThrows(ServerException.class, () -> weatherClient.getTodayWeather());
    }

    @Test
    void 날씨_데이터가_없으면_예외가_발생한다() {
        // Given
        ResponseEntity<WeatherDto[]> responseEntity = new ResponseEntity<>(new WeatherDto[]{}, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WeatherDto[].class)))
                .thenReturn(responseEntity);

        // When & Then
        assertThrows(ServerException.class, () -> weatherClient.getTodayWeather());
    }

    @Test
    void 정상적으로_날씨_데이터를_반환한다() {
        // Given
        WeatherDto[] weatherData = {new WeatherDto("Sunny", "25°C")};
        ResponseEntity<WeatherDto[]> responseEntity = new ResponseEntity<>(weatherData, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WeatherDto[].class)))
                .thenReturn(responseEntity);

        // When
        WeatherDto[] result = weatherClient.getTodayWeather();

        // Then
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("Sunny", result[0].getWeather());
    }
}

