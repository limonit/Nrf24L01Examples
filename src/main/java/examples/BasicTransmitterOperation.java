package examples;

import com.pi4j.io.gpio.*;
import pl.elektrofanklub.nrf24l01.Nrf24L01Communicator;
import pl.elektrofanklub.nrf24l01.Nrf24L01InterruptHandler;

import java.util.Arrays;

/**
 * Basic transmitter operation
 */
public class BasicTransmitterOperation {

    public static void main(String[] args) throws InterruptedException {
        final short address[] = {0xCE, 0xCE, 0xF1};

        final GpioController instance = GpioFactory.getInstance();
        final GpioPinDigitalOutput chipEnable = instance.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Chip enable", PinState.HIGH);

        Nrf24L01Communicator communicator = new Nrf24L01Communicator();
        communicator.init(0, 500000);
        communicator.setupTXAddress(address);
        communicator.setChipEnable(chipEnable);
        communicator.powerUp();
        communicator.enableTX();


        while (true) {
            Thread.sleep(5000);
            System.out.println("Sending packet");
            communicator.transmitPacket(new short[]{0, 2, 4, 6}, true);  // some example data
        }
    }
}
