package org.gogoboardTest.gui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
    private Thread threadExibirSensores;

    private void beep() {
        byte[] mensagem = new byte[64];
        mensagem[2] = 11;
        enviarMensagem(mensagem);
    }

    private void led(boolean ligar) {
        byte[] mensagem = new byte[64];
        mensagem[2] = 10;
        if (ligar) {
            mensagem[4] = 1;
        } else {
            mensagem[4] = 0;
        }
        enviarMensagem(mensagem);
    }

    private void exibeTextoCurto(String texto) throws Exception {
        if(texto.length() > 4){
            JOptionPane.showMessageDialog(this, "O display de segmentos não pode exibir mais de 4 characteres.", "Erro!", JOptionPane.ERROR_MESSAGE);
            //throw  new Exception("Erro, o display de segmentos não pode exibir mais de 4 characteres.");
        }
        byte[] mensagem = new byte[64];
        mensagem[2] = 60;
        for (int i = 0; i < texto.length(); i++) {
            mensagem[3+i] = (byte) texto.charAt(i);
        }
        enviarMensagem(mensagem);
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
        } catch (HidException e) {
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

    private void enviarMensagem(byte[] mensagem) {
        if (gogoBoard != null) {
            gogoBoard.write(mensagem, mensagem.length, (byte) 0);
        }
    }

    private byte[] receberMensagem(int numBytes) {
        byte[] mensagem = new byte[numBytes];
        if (gogoBoard != null) {
            gogoBoard.read(mensagem, 500);
            return mensagem;
        }
        return null;
    }

    private void carregarServicosHID() throws HidException {
        // Pegar os servicos HID e add listener
        servicosHID = HidManager.getHidServices();
        servicosHID.addHidServicesListener(this);

        // Percorre a lista dos dispositivos conectados
        for (HidDevice dispositivo : servicosHID.getAttachedHidDevices()) {
            if (dispositivo.getVendorId() == 0x461
                    && dispositivo.getProductId() == 0x20) {
                System.out.println("GoGo Board1: " + dispositivo);
                labelImagem.setIcon(comGoGo);
                gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
            }
        }
    }

    private void exibirSensores() {
        Runnable lerSensores;
        lerSensores = () -> {
            while (gogoBoard != null) {
                short[] sensores = lerSensores();
                labelSensor1.setText(Integer.toString(sensores[0]));
                labelSensor2.setText(Integer.toString(sensores[1]));
                labelSensor3.setText(Integer.toString(sensores[2]));
                labelSensor4.setText(Integer.toString(sensores[3]));
                labelSensor5.setText(Integer.toString(sensores[4]));
                labelSensor6.setText(Integer.toString(sensores[5]));
                labelSensor7.setText(Integer.toString(sensores[6]));
                labelSensor8.setText(Integer.toString(sensores[7]));
            }
        };
        threadExibirSensores = new Thread(lerSensores);
        threadExibirSensores.start();
        System.out.println("iniciando " + threadExibirSensores.getName());
    }

    public Gui() {
        initComponents();
        
        this.comGoGo = new ImageIcon("./imagens/board_found.png");
        this.semGoGo = new ImageIcon("./imagens/noboard_found.png");
        System.out.println("Iniciando Daemon...");

        try {
            carregarServicosHID();
            exibirSensores();
        } catch (HidException e) {
            System.err.println("HID Exception");
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar os serviços HID");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelImagem = new javax.swing.JLabel();
        labelSensor1 = new javax.swing.JLabel();
        labelSensor2 = new javax.swing.JLabel();
        labelSensor3 = new javax.swing.JLabel();
        labelSensor4 = new javax.swing.JLabel();
        labelSensor5 = new javax.swing.JLabel();
        labelSensor6 = new javax.swing.JLabel();
        labelSensor7 = new javax.swing.JLabel();
        labelSensor8 = new javax.swing.JLabel();
        botaoBeep = new javax.swing.JButton();
        botaoExibirTextoCurto = new javax.swing.JButton();
        botaoLed = new javax.swing.JToggleButton();
        textFieldExibirTextoCurto = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelImagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gogoboardTest/gui/imagens/noboard_found.png"))); // NOI18N

        labelSensor1.setText("0");

        labelSensor2.setText("0");

        labelSensor3.setText("0");

        labelSensor4.setText("0");

        labelSensor5.setText("0");

        labelSensor6.setText("0");

        labelSensor7.setText("0");

        labelSensor8.setText("0");

        botaoBeep.setText("Beep");
        botaoBeep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoBeepActionPerformed(evt);
            }
        });

        botaoExibirTextoCurto.setText("Exibir");
        botaoExibirTextoCurto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExibirTextoCurtoActionPerformed(evt);
            }
        });

        botaoLed.setText("Led On\n");
        botaoLed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labelSensor1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSensor8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botaoBeep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botaoLed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldExibirTextoCurto, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botaoExibirTextoCurto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(labelImagem, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(labelImagem)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botaoBeep)
                    .addComponent(botaoExibirTextoCurto)
                    .addComponent(botaoLed)
                    .addComponent(textFieldExibirTextoCurto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSensor1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSensor7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoBeepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoBeepActionPerformed
        System.out.println("Beep");
        beep();
    }//GEN-LAST:event_botaoBeepActionPerformed

    private void botaoExibirTextoCurtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExibirTextoCurtoActionPerformed
        try {
            exibeTextoCurto(textFieldExibirTextoCurto.getText());
        } catch (Exception ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botaoExibirTextoCurtoActionPerformed

    private void botaoLedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLedActionPerformed
        if (botaoLed.isSelected()) {
            led(true);
            System.out.println("Led On");
            botaoLed.setText("Led Off");
        } else {
            led(false);
            System.out.println("Led Off");
            botaoLed.setText("Led On");
        }
    }//GEN-LAST:event_botaoLedActionPerformed

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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JButton botaoBeep;
    private javax.swing.JButton botaoExibirTextoCurto;
    private javax.swing.JToggleButton botaoLed;
    private javax.swing.JLabel labelImagem;
    private javax.swing.JLabel labelSensor1;
    private javax.swing.JLabel labelSensor2;
    private javax.swing.JLabel labelSensor3;
    private javax.swing.JLabel labelSensor4;
    private javax.swing.JLabel labelSensor5;
    private javax.swing.JLabel labelSensor6;
    private javax.swing.JLabel labelSensor7;
    private javax.swing.JLabel labelSensor8;
    private javax.swing.JTextField textFieldExibirTextoCurto;
    // End of variables declaration//GEN-END:variables

    @Override
    public void hidDeviceAttached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            labelImagem.setIcon(comGoGo);
            gogoBoard = servicosHID.getHidDevice(0x461, 0x20, null);
            exibirSensores();
        }
    }

    @Override
    public void hidDeviceDetached(HidServicesEvent hse) {
        if (hse.getHidDevice().getVendorId() == 0x461
                && hse.getHidDevice().getProductId() == 0x20) {
            System.out.println("GoGo Board: " + hse.getHidDevice());
            labelImagem.setIcon(semGoGo);
            gogoBoard = null;
            System.out.println("destruindo " + threadExibirSensores.getName());
            threadExibirSensores.destroy();
        }
    }

    @Override
    public void hidFailure(HidServicesEvent hse) {
        System.err.println("Falha no HID");
    }
}
