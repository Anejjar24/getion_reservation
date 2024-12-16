package ma.ensaj.GestionReservation.controllers;
import io.grpc.stub.StreamObserver;


import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.services.ReservationService;
import ma.ensaj.GestionReservation.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.stream.Collectors;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;

import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.services.ReservationService;








@GrpcService
public class ReservationServiceImpl



        extends ReservationServiceGrpc.ReservationServiceImplBase {

    private final ReservationService reservationService;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    public ReservationServiceImpl(
            ReservationService reservationService,
            ClientRepository clientRepository,
            ChambreRepository chambreRepository
    ) {
        this.reservationService = reservationService;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    @Override
    public void createReservation(
            CreateReservationRequest request,
            StreamObserver<ReservationResponse> responseObserver
    ) {
        try {
            // Convertir le client gRPC en entité
            Client clientEntity = clientRepository.findById(request.getClient().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));

            // Convertir la chambre gRPC en entité
            Chambre chambreEntity = chambreRepository.findById(request.getChambre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Chambre not found"));

            // Créer la réservation
            Reservation reservation = new Reservation();
            reservation.setClient(clientEntity);
            reservation.setChambre(chambreEntity);
            reservation.setDateDebut(request.getDateDebut());
            reservation.setDateFin(request.getDateFin());
            reservation.setPreferences(request.getPreferences());

            Reservation savedReservation = reservationService.createReservation(reservation);

            // Convertir l'entité de réservation en message gRPC
            ma.ensaj.GestionReservation.stubs.Reservation grpcReservation = convertToGrpcReservation(savedReservation);

            ReservationResponse response = ReservationResponse.newBuilder()
                    .setReservation(grpcReservation)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateReservation(
            UpdateReservationRequest request,
            StreamObserver<ReservationResponse> responseObserver
    ) {
        try {
            // Convertir le client gRPC en entité
            Client clientEntity = clientRepository.findById(request.getClient().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));

            // Convertir la chambre gRPC en entité
            Chambre chambreEntity = chambreRepository.findById(request.getChambre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Chambre not found"));

            // Créer la réservation mise à jour
            Reservation updatedReservation = new Reservation();
            updatedReservation.setClient(clientEntity);
            updatedReservation.setChambre(chambreEntity);
            updatedReservation.setDateDebut(request.getDateDebut());
            updatedReservation.setDateFin(request.getDateFin());
            updatedReservation.setPreferences(request.getPreferences());

            Reservation savedReservation = reservationService.updateReservation(request.getId(), updatedReservation);

            // Convertir l'entité de réservation en message gRPC
            ma.ensaj.GestionReservation.stubs.Reservation grpcReservation = convertToGrpcReservation(savedReservation);

            ReservationResponse response = ReservationResponse.newBuilder()
                    .setReservation(grpcReservation)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteReservation(
            GetReservationByIdRequest request,
            StreamObserver<ReservationResponse> responseObserver
    ) {
        try {
            reservationService.deleteReservation(request.getId());

            // Retourner une réponse vide ou avec un message de succès
            ReservationResponse response = ReservationResponse.getDefaultInstance();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getReservationById(
            GetReservationByIdRequest request,
            StreamObserver<ReservationResponse> responseObserver
    ) {
        try {
            Reservation reservation = reservationService.getReservationById(request.getId());

            // Convertir l'entité de réservation en message gRPC
            ma.ensaj.GestionReservation.stubs.Reservation grpcReservation = convertToGrpcReservation(reservation);

            ReservationResponse response = ReservationResponse.newBuilder()
                    .setReservation(grpcReservation)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getReservationsByClient(
            GetReservationsByClientRequest request,
            StreamObserver<ReservationsResponse> responseObserver
    ) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByClient(request.getClientId());

            // Convertir les entités de réservation en messages gRPC
            List<ma.ensaj.GestionReservation.stubs.Reservation> grpcReservations = reservations.stream()
                    .map(this::convertToGrpcReservation)
                    .collect(Collectors.toList());

            ReservationsResponse response = ReservationsResponse.newBuilder()
                    .addAllReservations(grpcReservations)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getReservationsByDateRange(
            GetReservationsByDateRangeRequest request,
            StreamObserver<ReservationsResponse> responseObserver
    ) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByDateRange(
                    request.getStartDate(),
                    request.getEndDate()
            );

            // Convertir les entités de réservation en messages gRPC
            List<ma.ensaj.GestionReservation.stubs.Reservation> grpcReservations = reservations.stream()
                    .map(this::convertToGrpcReservation)
                    .collect(Collectors.toList());

            ReservationsResponse response = ReservationsResponse.newBuilder()
                    .addAllReservations(grpcReservations)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllReservations(
            GetAllReservationsRequest request,
            StreamObserver<ReservationsResponse> responseObserver
    ) {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();

            // Convertir les entités de réservation en messages gRPC
            List<ma.ensaj.GestionReservation.stubs.Reservation> grpcReservations = reservations.stream()
                    .map(this::convertToGrpcReservation)
                    .collect(Collectors.toList());

            ReservationsResponse response = ReservationsResponse.newBuilder()
                    .addAllReservations(grpcReservations)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    // Méthode utilitaire pour convertir une entité Reservation en message gRPC Reservation
    private ma.ensaj.GestionReservation.stubs.Reservation convertToGrpcReservation(Reservation reservation) {
        // Convertir le client
        ma.ensaj.GestionReservation.stubs.Client grpcClient = ma.ensaj.GestionReservation.stubs.Client.newBuilder()
                .setId(reservation.getClient().getId())
                .setNom(reservation.getClient().getNom())
                .setPrenom(reservation.getClient().getPrenom())
                .setEmail(reservation.getClient().getEmail())
                .setTelephone(reservation.getClient().getTelephone())
                .build();

        // Convertir la chambre
        ma.ensaj.GestionReservation.stubs.Chambre grpcChambre = ma.ensaj.GestionReservation.stubs.Chambre.newBuilder()
                .setId(reservation.getChambre().getId())
                .setPrix(reservation.getChambre().getPrix())
                .setDisponible(reservation.getChambre().getDisponible())
                .setType(ma.ensaj.GestionReservation.stubs.TypeChambre.valueOf(reservation.getChambre().getType().name()))
                .build();

        // Convertir la réservation
        return ma.ensaj.GestionReservation.stubs.Reservation.newBuilder()
                .setId(reservation.getId())
                .setClient(grpcClient)
                .setChambre(grpcChambre)
                .setDateDebut(reservation.getDateDebut())
                .setDateFin(reservation.getDateFin())
                .setPreferences(reservation.getPreferences())
                .build();
    }









}