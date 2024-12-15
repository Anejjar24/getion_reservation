package ma.ensaj.GestionReservation.config;


import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import lombok.AllArgsConstructor;

import ma.ensaj.GestionReservation.ws.ChambreSoapService;
import ma.ensaj.GestionReservation.ws.ClientSoapService;
import ma.ensaj.GestionReservation.ws.ReservationSoapService;
import ma.ensaj.GestionReservation.ws.UtilisateurSoapService;
import org.apache.cxf.bus.spring.SpringBus;
import java.util.ArrayList;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import ma.ensaj.GestionReservation.ws.ReservationSoapService;
import ma.ensaj.GestionReservation.ws.ClientSoapService;
import ma.ensaj.GestionReservation.ws.ChambreSoapService;
import ma.ensaj.GestionReservation.ws.UtilisateurSoapService;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private ReservationSoapService reservationSoapService;
    @Autowired
    private ClientSoapService clientSoapService;
    @Autowired
    private ChambreSoapService chambreSoapService;
    @Autowired
    private UtilisateurSoapService utilisateurSoapService;
    @Bean
    public List<EndpointImpl> endpoints() {
        List<EndpointImpl> endpoints = new ArrayList<>();

        EndpointImpl reservationEndpoint = new EndpointImpl(bus, reservationSoapService);
        reservationEndpoint.publish("/reservation");
        endpoints.add(reservationEndpoint);

        EndpointImpl clientEndpoint = new EndpointImpl(bus, clientSoapService);
        clientEndpoint.publish("/client");
        endpoints.add(clientEndpoint);

        EndpointImpl chambreEndpoint = new EndpointImpl(bus, chambreSoapService);
        chambreEndpoint.publish("/chambre");
        endpoints.add(chambreEndpoint);

        EndpointImpl utilisateurEndpoint = new EndpointImpl(bus, utilisateurSoapService);
        utilisateurEndpoint.publish("/utilisateur");
        endpoints.add(utilisateurEndpoint);

        return endpoints;
    }

    @Bean
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/services/*");
    }
}
