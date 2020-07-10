package com.shinstealer.aws.kinesis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;

@Service
@Slf4j
public class KinesisConsumerService {

    @Value("${shutdown-wait-sec:10}")
    public int shutDownWaitSec;

    @Value("${kinesis-sleep-msec:100}")
    private int kinesisSleepMsec;

    @Value("${kinesis-thread-priority:5}")
    private int kinesisThreadPriority;

    @Value("${kinesis-thread-max-num:2}")
    private int kinesisThreadMaxNum;

    @Value("${kinesis-thread-consumer-name:}")
    private String kinesisThreadConsumerName;

    @Value("${kinesis-thread-executor-priority:8}")
    private int kinesisThreadExecutorPriority;

    @Value("${kinesis-thread-executor-num:5}")
    private int kinesisThreadExecutorNum;

    @Value("${kinesis-lock-basekey:}")
    private String kinesisLockBasekey;

    @Value("${kinesis-lock-sec:300}")
    private int kinesisLockSec;

    @Value("${kinesis-latest-serialno-basekey:}")
    private String kinesisLatestSerialnoBasekey;

    @Value("${service.kinesis-event.arn:}")
    public String kinesisEventArn;

    @Value("${service.kinesis-event.shardid:}")
    public String kinesisEventShardid;

    /**
     * thread
     */
    private List<KinesisConsumerThread> kinesisConsumerThreads = null;

    AwsBasicCredentials awsBasicCredentials = null;

    int counter = 0;

    private ExecutorService executorService;

    @PostConstruct
    public void initialize() {
        awsBasicCredentials = AwsBasicCredentials.create("accessKeyId", "secretAccessKey");
        executorService = Executors.newFixedThreadPool(kinesisThreadExecutorNum, new ThreadFactory() {

            @Override
            public Thread newThread(final Runnable r) {
                final Thread thread = new Thread(r, "Test Kinesis Work" + counter++);
                thread.setPriority(kinesisThreadExecutorPriority);
                return thread;
            }

        });

        int threadCreateNum = kinesisEventShardid.length();

        if (threadCreateNum > kinesisThreadMaxNum) {
            threadCreateNum = kinesisThreadMaxNum;
        }

        kinesisConsumerThreads = new ArrayList<>();
        List<String> shardIds = new ArrayList<>();
        for (int i = 0; i < threadCreateNum; i++) {
            KinesisConsumerThread kinesisConsumerThread = new KinesisConsumerThread(i+1, 
            shardIds, "streamArn", "consumerName");
            kinesisConsumerThreads.add(kinesisConsumerThread);
            
            kinesisConsumerThread.setPriority(kinesisThreadPriority);
            kinesisConsumerThread.start();;
            
        }

    }

    public class KinesisConsumerThread extends Thread {
        
        
        private int id;
        
        private List<String> shardIds = null;
        
        private String streamArn = null;
        
        private String consumerName = null;

        private boolean continueFlag = true;
        
        private String consumerARN = null;
        
        private KinesisAsyncClient kinesisAsyncClient = null;
        
        private KinesisClient kinesisClient = null;
        
        SubscribeToShardRequest request = null;
        
        UUID uuid = null;
        
        String sequenceNumber = null;
        
        String targetShardId = null;

        private final Region clientRegion = Region.AP_NORTHEAST_1;

        public KinesisConsumerThread(int id, List<String> shardIds, String streamArn, String consumerName) {
            this.id = id;
            this.shardIds = shardIds;
            this.streamArn = streamArn;
            this.consumerName = consumerName;

            this.setName("kinesis Thread" + String.format("%02d", this.id));

        }

        public void shutdown() {
            continueFlag = false;
            kinesisAsyncClient.close();
        }

        private boolean initialize() {

            try {
                kinesisAsyncClient = KinesisAsyncClient.builder().region(clientRegion)
                        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build();
                kinesisClient = KinesisClient.builder().region(clientRegion)
                        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build();
                consumerARN = "arn";
                if (consumerARN == null) {
                    return false;

                }
                uuid = UUID.randomUUID();

            } catch (Exception e) {
                
                return false;
            }
            return true;

        }

        @Override
        public void run() {
            try {
                if (this.initialize() == false) {
                    log.error("stop thread for failure");
                    return;
                }
                while (true) {
                    if (continueFlag == false) {
                        break;
                    }

                    SubscribeToShardRequest request = this.getRequest();
                    if (request == null) {
                        log.info("Sleep 10 sec");
                        TimeUnit.SECONDS.sleep(10);
                        continue;
                    } else {

                    }

                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                log.info("delete");
            }
            log.info(this.getName() + "is finished");
        }

        private SubscribeToShardRequest getRequest() {
            return null;
        }

    }
}
