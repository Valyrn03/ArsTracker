package application.utils;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class MockIdGenerator implements IIdGenerator {
    @Getter List<String> mockIds;
    int counter;

    public MockIdGenerator(List<String> ids){
        this.mockIds = ids;
        counter = 0;
    }

    @Override
    public UUID getUUID() {
        return UUID.fromString(mockIds.get(counter++)); //functions like a "get and increment"
    }
}
