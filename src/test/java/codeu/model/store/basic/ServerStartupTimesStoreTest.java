package codeu.model.store.basic;

import codeu.model.data.ServerStartupTimes;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerStartupTimesStoreTest {
  private ServerStartupTimesStore serverStartupTimesStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final ServerStartupTimes serverStartupTimes = new ServerStartupTimes(UUID.randomUUID(), Instant.ofEpochMilli(1000), Instant.ofEpochMilli(2000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    serverStartupTimesStore = serverStartupTimesStore.getTestInstance(mockPersistentStorageAgent);

    serverStartupTimesStore.setServerStartupTimes(serverStartupTimes);
  }

  @Test
  public void testGetServerStartupTimes() {
    ServerStartupTimes resultServerStartupTimes = serverStartupTimesStore.getServerStartupTimes();

    assertEquals(serverStartupTimes, resultServerStartupTimes);
  }

  @Test
  public void testUpdateServerStartupTimes() {

    ServerStartupTimes inputServerStartupTimes = new ServerStartupTimes(UUID.randomUUID(), Instant.ofEpochMilli(3000), Instant.ofEpochMilli(4000));
    serverStartupTimesStore.updateServerStartupTimes(inputServerStartupTimes);

    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputServerStartupTimes);
  }

  private void assertEquals(ServerStartupTimes expectedServerStartupTimes, ServerStartupTimes actualServerStartupTimes) {
    System.out.println(expectedServerStartupTimes.getServerStartupTimesId());
    System.out.println(actualServerStartupTimes.getServerStartupTimesId());
    Assert.assertEquals(expectedServerStartupTimes.getServerStartupTimesId(), actualServerStartupTimes.getServerStartupTimesId());
    Assert.assertEquals(expectedServerStartupTimes.getReferenceServerStartupTime(), actualServerStartupTimes.getReferenceServerStartupTime());
    Assert.assertEquals(expectedServerStartupTimes.getCurrentServerStartupTime(), actualServerStartupTimes.getCurrentServerStartupTime());

  }
}
