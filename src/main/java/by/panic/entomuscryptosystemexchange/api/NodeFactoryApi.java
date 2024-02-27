package by.panic.entomuscryptosystemexchange.api;

import by.panic.entomuscryptosystemexchange.api.payload.nodeFactory.*;
import by.panic.entomuscryptosystemexchange.exception.NodeFactoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

@Component
@RequiredArgsConstructor
@Slf4j
public class NodeFactoryApi {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${nodeFactoryApi.url}")
    private String URL;

    public NodeFactoryReceiveResponse receive(NodeFactoryReceiveRequest nodeFactoryReceiveRequest) {
        ResponseEntity<String> nodeFactoryReceiveRequestResponseEntity = null;

        nodeFactoryReceiveRequestResponseEntity = restTemplate.postForEntity(URL + "/api/receive", nodeFactoryReceiveRequest,
                String.class);

        if (nodeFactoryReceiveRequestResponseEntity.getBody().equals("failed to create new transactor: transactor not found")) {
            throw new NodeFactoryException("You have entered an invalid Network-Token pair");
        }

        NodeFactoryReceiveResponse nodeFactoryReceiveResponse = null;

        try {
            nodeFactoryReceiveResponse = objectMapper.readValue(nodeFactoryReceiveRequestResponseEntity.getBody(),
                    NodeFactoryReceiveResponse.class);
        } catch (Exception e) {
            log.warn(e.getMessage());

            throw new NodeFactoryException("NodeFactory service system error");
        }

        return nodeFactoryReceiveResponse;
    }

    public NodeFactoryGetStatusResponse getStatus(long receiveRequestId) {
        ResponseEntity<NodeFactoryGetStatusResponse> nodeFactoryGetStatusResponseResponseEntity = null;

        try {
            nodeFactoryGetStatusResponseResponseEntity = restTemplate.getForEntity(URL + "/api/status?id=" + receiveRequestId,
                    NodeFactoryGetStatusResponse.class);
        } catch (UnknownContentTypeException ignored) {
            return null;
        }

        return nodeFactoryGetStatusResponseResponseEntity.getBody();
    }

    public NodeFactorySendResponse send(NodeFactorySendRequest nodeFactorySendRequest) {
        ResponseEntity<String> nodeFactorySendResponseResponseEntity = null;

        nodeFactorySendResponseResponseEntity = restTemplate.postForEntity(URL + "/api/send", nodeFactorySendRequest,
                String.class);

        if (nodeFactorySendResponseResponseEntity.getBody().equals("failed to execute send: failed to send trx: failed to get dest: checksum error")) {
            throw new NodeFactoryException("You have entered an incorrect network address");
        } else if (nodeFactorySendResponseResponseEntity.getBody().equals("failed to create new transactor: transactor not found")) {
            throw new NodeFactoryException("You have entered an invalid Network-Token pair");
        }

        NodeFactorySendResponse nodeFactorySendResponse = null;

        try {
            nodeFactorySendResponse = objectMapper.readValue(nodeFactorySendResponseResponseEntity.getBody(),
                    NodeFactorySendResponse.class);
        } catch (Exception e) {
            log.warn(e.getMessage());

            throw new NodeFactoryException("NodeFactory service system error");
        }

        return nodeFactorySendResponse;
    }
}
