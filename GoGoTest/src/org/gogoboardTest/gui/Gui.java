package org.gogoboardTest.gui;

import javax.swing.ImageIcon;
import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class Gui extends javax.swing.JFrame implements HidServicesListener {

    private ImageIcon comGoGo;
    private ImageIcon semGoGo;
    private HidServices servicosHID;
    private HidDevice gogoBoard;

    private void carregarServicosHID() throws HidException {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);

        // Percorre a lista dos dispositivos conectados
        for (HidDevice dispositivo : servicosHID.getAttachedHidDevices()) {
            if (dispositivo.getVendorId() == 0x461
                    && dispositivo.getProductId() == 0x20) {
                System.out.println("GoGoBoard: " + dispositivo);
                labelImagem.setIcon(comGoGo);
                gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
            }
        }
    }

    public Gui() {
        initComponents();
        this.comGoGo = new ImageIcon("./imagens/board_found.png");
        this.semGoGo = new ImageIcon("./imagens/noboard_found.png");
        System.out.println("Iniciando Daemon...");
        
        try {
            carregarServicosHID();
        }catch (Exception e){
            System.err.println("HID Exception");
            e.printStackTrace();
            throw  new RuntimeException("Erro ao carregar os serviços HID");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelImagem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelImagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gogoboardTest/gui/imagens/noboard_found.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(labelImagem)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(labelImagem)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelImagem;
    // End of variables declaration//GEN-END:variables

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGoBoard: " + hse.getHidDevice());
            labelImagem.setIcon(comGoGo);
            gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGoBoard: " + hse.getHidDevice());
            labelImagem.setIcon(semGoGo);
            gogoBoard = null;
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        System.err.println("Falha no HID");
    }
}
