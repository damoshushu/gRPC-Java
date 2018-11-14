package com.kk.grpc.serverdemo;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.nuance.protobuf.tts.v1.TTSRequest;
import com.nuance.protobuf.tts.v1.TTSServiceGrpc;
import com.nuance.protobuf.tts.v1.TTSStreamingResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TTSServiceImpl extends TTSServiceGrpc.TTSServiceImplBase {

    @Override
    public void speak(TTSRequest request, StreamObserver<TTSStreamingResponse> responseObserver) {
        log.debug("Get TTS Request");

        byte[] audioData = {12, 13, 14, 15, 16};
        TTSStreamingResponse.AudioElement audioElement = TTSStreamingResponse.AudioElement.
                newBuilder().setCodec("PCM")
                .setData(ByteString.copyFrom(audioData))
                .setPacketDurationMilliseconds(5).build();

        TTSStreamingResponse response = TTSStreamingResponse.newBuilder().setAudio(audioElement).build();
        responseObserver.onNext(response);

        byte[] audioData2 = {22, 33, 44, 55, 66};
        TTSStreamingResponse.AudioElement audioElement2 = TTSStreamingResponse.AudioElement.
                newBuilder().setCodec("PCM2")
                .setData(ByteString.copyFrom(audioData2))
                .setPacketDurationMilliseconds(5).build();

        TTSStreamingResponse response2 = TTSStreamingResponse.newBuilder().setAudio(audioElement2).build();
        responseObserver.onNext(response2);

        responseObserver.onCompleted();
    }

}
