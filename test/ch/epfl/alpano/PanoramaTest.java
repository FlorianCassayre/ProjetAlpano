package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;
import static java.lang.Math.toRadians;

public class PanoramaTest {
    private static int width =2500, height=800; 
    private static double v_h = toRadians(27), a = toRadians(162);
    private static PanoramaParameters test = new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)), 1380, a, v_h, 300, width, height);

    @Test
    public void azimuthForXCorners() {
        assertEquals(a - v_h/2, test.azimuthForX(0), 1e-3);
        assertEquals(a + v_h/2, test.azimuthForX(width - 1), 1e-3);
        assertEquals(a, test.azimuthForX(width/2 - 1), 1e-3);
    }

    @Test
    public void xForAzimuthCorners() {
        assertEquals(0, test.xForAzimuth(a - v_h/2), 1e-3);
        assertEquals((width - 1)/2., test.xForAzimuth(a), 1e-3);
        assertEquals(width -1, test.xForAzimuth(a + v_h/2), 1e-3);
    }
    
    @Test
    public void altitudeForYCorners() {
        assertEquals(0, test.altitudeForY(height/2 - 1), 1e-3);
        assertEquals(test.verticalFieldOfView() / 2, test.altitudeForY(0), 1e-3);
        assertEquals(-test.verticalFieldOfView() / 2, test.altitudeForY(height - 1), 1e-3);
    }
    
    @Test 
    public void yForAltitudeCorners() {
        assertEquals(0, test.yForAltitude(test.verticalFieldOfView() / 2), 1e-3);
        assertEquals((height - 1)/ 2. , test.yForAltitude(0), 1e-3);
        assertEquals(height - 1, test.yForAltitude(-test.verticalFieldOfView()  /2), 1e-3);
    }
    
    //////////Tests based on step 6 
}
