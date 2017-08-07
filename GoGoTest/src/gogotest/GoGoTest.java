package gogotest;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class GoGoTest implements HidServicesListener{
    private HidServices servicosHID;
    private HidDevice gogoBoard;

    private void carregarServicosHID(){
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);
        
        // Percorre a lista dos dispositivos conectados
        for (HidDevice dispositivo : servicosHID.getAttachedHidDevices()) {
            if (dispositivo.getVendorId() == 0x461 && dispositivo.getProductId() == 0x20) {
                System.out.println("GoGoBoard: " + dispositivo);
                gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Iniciando daemon...");
        GoGoTest g = new GoGoTest();
        g.carregarServicosHID();
    }

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
