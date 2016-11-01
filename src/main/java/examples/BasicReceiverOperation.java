package examples;

import com.pi4j.io.gpio.*;
import pl.elektrofanklub.nrf24l01.Nrf24L01Communicator;

import java.util.Arrays;

/**
 * Basic Receiver Operation
 */
public class BasicReceiverOperation {
    public static void main(String[] args) throws InterruptedException {
        final short address[] = {0xCE, 0xCE, 0xF1};
        short[] databuffer = new short[32];

        final GpioController instance = GpioFactory.getInstance();
        final GpioPinDigitalOutput chipEnable = instance.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Chip enable", PinState.HIGH);

        Nrf24L01Communicator communicator = new Nrf24L01Communicator();
        communicator.init(0, 500000);
        communicator.setupRXAddress(address, 0);
        communicator.setChipEnable(chipEnable);
        communicator.enableRX();
        communicator.powerUp();

        while (true) {
            Thread.sleep(2000);
            while (!communicator.isRxFifoEmpty()) {
                communicator.readPacket(databuffer);
                System.out.println(Arrays.toString(databuffer));

            }
        }
    }
}
