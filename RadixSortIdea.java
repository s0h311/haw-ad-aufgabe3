
import java.util.LinkedList;
import java.util.Queue;


// RadixSort for long[] configured for 4bit-slices/nibbles
// Demo to explain RadixSort idea via source code
//
// abbreviations
// -------------
// bIdxOldQl ::= bucket index (of) old queue_list
// bIdxCurQl ::= bucket index (of) current queue_list
public class RadixSortIdea {
    
    // this implementation works with 4bit slices (nibbles) resulting in 2^4=16 "buckets" resp. queues
    private static int sliceSize = 4;
    private static int sliceCnt = 64/sliceSize;     // 64/4 = 16 ,  long has 64bit and nibbles/slices have 4 bits => 16slices resp. runs
    private static int bucketCnt = 1 << sliceSize;  //  2^4 = 16 "buckets" (resp. queues) are required
    private static int sliceMask = bucketCnt-1;     // 16-1 = 0xF is mask for identification of "related" slice (resp. bucket/queue)
    
    
    
    public void sort( final long[] arrayToBeSorted ){
        @SuppressWarnings("unchecked")
        final Queue<Long>[][] queueMatrix = ( Queue<Long>[][])( new Queue[2][bucketCnt] );
        for( int queueListSelectionIdx=2; --queueListSelectionIdx>=0; ){
            for( int bc=bucketCnt; --bc>=0; ){
                queueMatrix[queueListSelectionIdx][bc] = new LinkedList<Long>();    // double linked list !!!
            }//for
        }//for
        
        int oldQueueList = 0b0;
        int curQueueList = 0b1;
        int shift = 0;
        
        // 1st sort step
        for( final long curValue : arrayToBeSorted ){
            final int bIdxCurQl = (int)( (curValue >>> shift) & sliceMask );
            queueMatrix[curQueueList][bIdxCurQl].add( curValue );
        }//for
        
        // other sort steps
        for( int stillToDo=sliceCnt; --stillToDo>0; ){
            // prepare for next sort step
            shift += sliceSize;
            curQueueList ^= 0b1;    // new/current queue list taking values from current run
            oldQueueList ^= 0b1;    // old queue list containing values from last run
            
            // current sort step
            for( int bIdxOldQl=0; bIdxOldQl<bucketCnt; bIdxOldQl++ ){
                while( ! queueMatrix[oldQueueList][bIdxOldQl].isEmpty() ){
                    final long curValue = queueMatrix[oldQueueList][bIdxOldQl].remove();
                    final int bIdxCurQl = (int)( (curValue >>> shift) & sliceMask );
                    queueMatrix[curQueueList][bIdxCurQl].add( curValue );
                }//while
            }//for
        }//for
        
        // put sorted values back into array
        int arrayIdx = 0;
        for( int bIdxCurQl=0; bIdxCurQl<bucketCnt; bIdxCurQl++ ){
            while( ! queueMatrix[curQueueList][bIdxCurQl].isEmpty() ){
                arrayToBeSorted[arrayIdx++] = queueMatrix[curQueueList][bIdxCurQl].remove();
            }//while
        }//for
        
    }//method()
    
}//class
