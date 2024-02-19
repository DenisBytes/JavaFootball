package com.github.denisbytes.javafootball.score_calculator.grpc;

import com.github.denisbytes.javafootball.score_calculator.proto.MatchServiceProto;
import com.github.denisbytes.javafootball.score_calculator.store.Store;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class MatchService {

    private final Store store = Store.getStore();

    public void getMatches(MatchServiceProto.GetMatchesRequest request, StreamObserver<MatchServiceProto.GetMatchesResponse> responseObserver) {
        MatchServiceProto.GetMatchesResponse.Builder responseBuilder = MatchServiceProto.GetMatchesResponse.newBuilder();
        responseBuilder.addAllMatches(store.getMatches());
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}