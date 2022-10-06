import testAPIstep.StepsRM;
import org.junit.jupiter.api.Test;

public class dz5TestMain extends StepsRM {

    @Test
    public void RM_Test()
    {
        getIdPers("Morty Smit");
        getInfoPers();
        getLastPers();
        getLastPersInfo();

        AssertLocPers();
        AssertRacePers();

    }
}
