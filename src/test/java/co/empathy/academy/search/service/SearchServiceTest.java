package co.empathy.academy.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.controller.SearchController;
import co.empathy.academy.search.repositories.ElasticClient;
import co.empathy.academy.search.service.client.SearchEngineClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

// To unit test, we can mock the dependencies of the class we're testing through Mockito
@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    ElasticsearchCall elasticsearchCall;
    @Mock
    SearchService searchService;

    @InjectMocks
    SearchController searchController;

    @Test
    void givenQuery_whenSearch_thenReturnResult() throws IOException {

        /*
        String exampleQuery = "example query";
        String expectedResult = "Query: example query, Cluster name: docker-cluster";
        // We create a mock for the SearchEngineClient
        // We mock the call to executeQuery on the client
        // You can look at it like: "given this method is called with this parameter then it will return this result"
        System.out.println(searchController.search(exampleQuery));

        ResponseEntity result = searchController.search(exampleQuery);
        System.out.println(result.getBody());
        //assertEquals(expectedResult, result.getBody());
        // We assert that the result of what we're testing matches what we're expecting
        //assertEquals(expectedResult, result);

        // Sometimes it's useful to check whether specific methods haven been called on a mock, we can do that with Mockito
        //verify(client).getClusterName();
*/
    }

    @Test
    void givenQuery_whenErrorDuringSearch_thenLetItPropagate() throws IOException {
        /*
        String exampleQuery = "example query";
        SearchEngineClient client = mock(SearchEngineClient.class);
        // In this case we're going to simulate that the client throws an error, maybe it couldn't connect to Elastic
        given(client.executeQuery(exampleQuery)).willThrow(RuntimeException.class);

        SearchService searchService = new SearchServiceImpl(client);

        // In this case we just check that the exception bubbles up
        assertThrows(RuntimeException.class, () -> searchService.search(exampleQuery));

         */
    }

    @Test
    void givenBlankQuery_whenSearch_thenDoNotExecuteQueryAndReturnEmptyString() throws IOException {
        /*
        SearchEngineClient client = mock(SearchEngineClient.class);
        SearchService searchService = new SearchServiceImpl(client);
        String result = searchService.search("   ");

        assertTrue(result.isEmpty());
        // For this test we didn't need to mock any calls because we should've never called the client, we can check that with Mockito
        verifyNoInteractions(client);
        s
         */
    }
}