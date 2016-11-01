package examples;

import com.pi4j.io.gpio.*;
import pl.elektrofanklub.nrf24l01.Nrf24L01Communicator;
import pl.elektrofanklub.nrf24l01.Nrf24L01InterruptHandler;

import java.util.Arrays;

/**
 * Transmitter Operation
 */
public class TransmitterOperation {
    public static void main(String[] args) throws InterruptedException {
        final short address[] = {0xCE, 0xCE, 0xF1};

        final GpioController instance = GpioFactory.getInstance();
        final GpioPinDigitalOutput chipEnable = instance.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Chip enable", PinState.HIGH);
        final GpioPinDigitalInput interrupt = instance.provisionDigitalInputPin(RaspiPin.GPIO_06, "Interrupt");

        Nrf24L01Communicator communicator = new Nrf24L01Communicator();
        communicator.init(0, 500000);
        communicator.setupTXAddress(address);
        communicator.setChipEnable(chipEnable);
        communicator.setChannel(55);
        communicator.setDataRate(Nrf24L01Communicator.DataRate.DR250K);
        communicator.setTxPower(Nrf24L01Communicator.TxPower.DBM0);
        communicator.setInterrupt(interrupt, new Nrf24L01InterruptHandler() {
            @Override
            public void maxRT() {
                System.out.println("Transmission Error. Max RT. Flushing buffer.");
       //         communicator.flushTX();
            }

            @Override
            public void txDS() {
                System.out.println("Packet sent");
            }

            @Override
            public void rxDR() {

            }
        });
        communicator.enableTX();
        communicator.powerUp();

        while (true) {
            Thread.sleep(5000);
            System.out.println("Sending packet");
            communicator.transmitPacket(new short[]{0, 2, 4, 6}, true);  // some example data
        }
    }
}
