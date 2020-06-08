package sk.hazarth.walletsim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Provides additional tools useful for working with the CryptoCompare API
 */
public class CryptoCompareHelper {

    /**
     * Hides public constructor
     */
    private CryptoCompareHelper(){

    }

    /**
     * Crypto compare API has an upper limit of *characters* for some input parameters. This function partitions a
     * collection of string into subsets that will fit the parameter limit in a single call
     * @param collection
     * @param maxLength
     * @return
     */
    public static List<Collection<String>> partitionParamCollection(Collection<String> collection, int maxLength) {
        List<Collection<String>> partitions = new ArrayList<>();
        Iterator<String> it = collection.iterator();

        //prepare container and counter
        List<String> currentPartition = new ArrayList<>();
        int length = 0;
        while (it.hasNext()){
            String param=it.next();
            length += param.length() + 1; // +1 to count the comma (,) after each param

            //if fits, add to partition, otherwise finalize partition and create a new one adding the element into it instead
            if(length <= maxLength){
                currentPartition.add(param);
            }else{
                length = param.length();
                partitions.add(currentPartition);
                currentPartition = new ArrayList<>();
                currentPartition.add(param);
            }
        }
        partitions.add(currentPartition);

        return partitions;
    }

}
