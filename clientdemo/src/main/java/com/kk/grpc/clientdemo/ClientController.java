package com.kk.grpc.clientdemo;


import com.google.protobuf.ByteString;
import com.nuance.protobuf.tts.v1.TTSRequest;
import com.nuance.protobuf.tts.v1.TTSServiceGrpc;
import com.nuance.protobuf.tts.v1.TTSStreamingResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

@RestController
@Slf4j
public class ClientController {


    @RequestMapping(value = "/speak", method = RequestMethod.GET)
    public void checkTTS() {


        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 8088)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();

        TTSServiceGrpc.TTSServiceStub  ttsServiceStub = TTSServiceGrpc.newStub(managedChannel);
        TTSRequest request = TTSRequest.newBuilder().setTypeValue(5).build();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        StreamObserver<TTSStreamingResponse> responseObserver = new StreamObserver<TTSStreamingResponse>() {
            @Override
            public void onNext(TTSStreamingResponse result) {
                TTSStreamingResponse.AudioElement element = result.getAudio();
                String codec = element.getCodec();
                ByteString data = element.getData();
                byte[] arrdata=data.toByteArray();
                log.info("Get One Message:"+codec);
            }
            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("completed");
                countDownLatch.countDown();
            }

        };

        ttsServiceStub.speak(request, responseObserver);

        log.debug("All Completed!");

    }


}
