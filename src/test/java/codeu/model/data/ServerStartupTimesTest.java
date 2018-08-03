package codeu.model.data;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;


public class ServerStartupTimesTest {

  private UUID serverStartupTimesId;

  /** The current time as an Instant */
  private Instant currentTime;

  /** 1 year before five days after current date */
  private Instant oneYearFiveDaysBefore;

  /** 25 hours before currentTime */
  private Instant twentyfiveHoursBefore;

  /** 24 hours before currentTime */
  private Instant twentyfourHoursBefore;

  /** 20 hours before currentTime */
  private Instant twentyHoursBefore;

  /** 25 hours after currentTime */
  private Instant twentyfiveHoursAfter;

  /** Instance of serverStartupTimes used in following tests */
  private ServerStartupTimes serverStartupTimes;

  @Before
  public void setup() {

    ZonedDateTime currentDate = ZonedDateTime.now();

    currentTime = currentDate.toInstant();

    oneYearFiveDaysBefore = currentDate.minusYears(1).plusDays(5).toInstant();

    twentyfiveHoursBefore = currentDate.minusDays(1).toInstant();

    twentyfourHoursBefore = currentDate.minusDays(1).toInstant();

    twentyHoursBefore = currentDate.minusHours(20).toInstant();

    twentyfiveHoursAfter = currentDate.plusHours(25).toInstant();

    serverStartupTimesId = UUID.randomUUID();

    Instant referenceServerStartupTime = Instant.ofEpochMilli(1000);
    Instant currentServerStartupTime = Instant.ofEpochMilli(2000);
    serverStartupTimes = new ServerStartupTimes(serverStartupTimesId, referenceServerStartupTime, currentServerStartupTime);

  }

  @Test
  public void testCreate() {

    Assert.assertEquals(serverStartupTimesId, serverStartupTimes.getServerStartupTimesId());
    Assert.assertEquals(Instant.ofEpochMilli(1000), serverStartupTimes.getReferenceServerStartupTime());
    Assert.assertEquals(Instant.ofEpochMilli(2000), serverStartupTimes.getCurrentServerStartupTime());

  }


  @Test
  public void TestSetReferenceServerStartupTime() {

    serverStartupTimes.setReferenceServerStartupTime(Instant.ofEpochMilli(3000));
    Assert.assertEquals(Instant.ofEpochMilli(3000), serverStartupTimes.getReferenceServerStartupTime());

  }


  @Test
  public void testCompareTimesOneYearBeforeFiveDaysAfter() {

    serverStartupTimes.setReferenceServerStartupTime(oneYearFiveDaysBefore);
    serverStartupTimes.setCurrentServerStartupTime(currentTime);
    Assert.assertTrue(serverStartupTimes.compareStartupTimes24());

  }

  @Test
  public void testCompareTimesTwentyfiveHoursBefore() {

    serverStartupTimes.setReferenceServerStartupTime(twentyfiveHoursBefore);
    serverStartupTimes.setCurrentServerStartupTime(currentTime);
    Assert.assertTrue(serverStartupTimes.compareStartupTimes24());

  }

  @Test
  public void testCompareTimesTwentyfourHoursBefore() {

    serverStartupTimes.setReferenceServerStartupTime(twentyfourHoursBefore);
    serverStartupTimes.setCurrentServerStartupTime(currentTime);
    Assert.assertTrue(serverStartupTimes.compareStartupTimes24());

  }

  @Test
  public void testCompareTimesTwentyHoursBefore() {

    serverStartupTimes.setReferenceServerStartupTime(twentyHoursBefore);
    serverStartupTimes.setCurrentServerStartupTime(currentTime);
    Assert.assertFalse(serverStartupTimes.compareStartupTimes24());

  }

  @Test
  public void testCompareTimesTwentyFiveHoursAfter() {

    serverStartupTimes.setReferenceServerStartupTime(twentyfiveHoursAfter);
    serverStartupTimes.setCurrentServerStartupTime(currentTime);
    Assert.assertFalse(serverStartupTimes.compareStartupTimes24());

  }


}
