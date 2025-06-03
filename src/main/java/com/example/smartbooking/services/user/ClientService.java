package com.example.smartbooking.services.user;

import com.example.smartbooking.models.Client;
import com.example.smartbooking.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setAddress(clientDetails.getAddress());
        client.setPreferredLanguage(clientDetails.getPreferredLanguage());

        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        if (clientRepository.existsById(id) == false) {
            throw new RuntimeException("Client not found");
        }
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent() == false) {
            return client.get();
        }
        return null;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}