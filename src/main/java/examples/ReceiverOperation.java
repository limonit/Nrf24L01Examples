package examples;

import com.pi4j.io.gpio.*;
import pl.elektrofanklub.nrf24l01.Nrf24L01Communicator;
import pl.elektrofanklub.nrf24l01.Nrf24L01InterruptHandler;

import java.util.Arrays;

/**
 * Basic receiver operation
 */
public class ReceiverOperation {
    public static void main(String[] args) throws InterruptedException {
        final short address[] = {0xCE, 0xCE, 0xF1};
        short[] databuffer = new short[32];

        final GpioController instance = GpioFactory.getInstance();
        final GpioPinDigitalOutput chipEnable = instance.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Chip enable", PinState.HIGH);
        final GpioPinDigitalInput interrupt = instance.provisionDigitalInputPin(RaspiPin.GPIO_06, "Interrupt");


        Nrf24L01Communicator communicator = new Nrf24L01Communicator();
        communicator.init(0, 500000);
        communicator.setupRXAddress(address, 0);
        communicator.setChipEnable(chipEnable);
        communicator.setChannel(55);
        communicator.setDataRate(Nrf24L01Communicator.DataRate.DR250K);
        communicator.setTxPower(Nrf24L01Communicator.TxPower.DBM0);
        communicator.setInterrupt(interrupt, new Nrf24L01InterruptHandler() {
            @Override
            public void maxRT() {

            }

            @Override
            public void txDS() {

            }

            @Override
            public void rxDR() {
                System.out.println("Packet received!");
                while (!communicator.isRxFifoEmpty()) {
                    communicator.readPacket(databuffer);
                    System.out.println(Arrays.toString(databuffer));
                }
            }
        });
        communicator.enableRX();
        communicator.powerUp();
        System.out.println("started");
        while (true) {
            // do something
        }
    }
}
