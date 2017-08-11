package org.gogoboardTest.gui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    private HidServices servicosHID;
    private HidDevice gogoBoard;
    private ImageIcon comGoGo;
    private ImageIcon semGoGo;

    private byte[] receberMensagem(int numBytes) {
        byte[] mensagem = new byte[numBytes];
        if (gogoBoard != null) {
            gogoBoard.read(mensagem, 500);
            return mensagem;
        }
        return null;
    }

    private short[] lerSensores() {
        short[] sensores = new short[8];
        try {
            byte[] data;
            do {
                data = receberMensagem(64);
            } while (data[0] != 0);       // Evitar pegar valor zerado do sensor

            for (int i = 0; i < 8; i++) {
                ByteBuffer bb = ByteBuffer.wrap(data, (2 * i) + 1, 2);
                bb.order(ByteOrder.BIG_ENDIAN);
                sensores[i] = bb.getShort();
            }
        } catch (Exception e) {
            System.err.println("Não foi possivel ler os dados da GoGo Board");
        }
        return sensores;
    }

    private int lerSensor(int numSensor) {
        if (numSensor >= 1 || numSensor <= 8) {
            return lerSensores()[numSensor - 1];
        }
        return -1;
    }

    private void carregarServicosHID() throws HidException {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);

        // Percorre a lista dos dispositivos conectados
        for (HidDevice dispositivo : servicosHID.getAttachedHidDevices()) {
            if (dispositivo.getVendorId() == 0x461
                    && dispositivo.getProductId() == 0x20) {
                System.out.println("GoGo Board: " + dispositivo);
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
            //lerSensores();
            for (int i = 0; i < 100; i++) {
                System.out.println(lerSensor(1));
            }

        } catch (Exception e) {
            System.err.println("HID Exception");
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar os serviços HID");
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
            System.out.println("GoGo Board: " + hse.getHidDevice());
            labelImagem.setIcon(comGoGo);
            gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            labelImagem.setIcon(semGoGo);
            gogoBoard = null;
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        System.err.println("Falha no HID");
    }
}
