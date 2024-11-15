package org.example.backend.services.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Customer;
import org.example.backend.db.entites.Moderator;
import org.example.backend.db.repositories.CustomerRepository;
import org.example.backend.db.repositories.ModeratorRepository;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.dto.responses.MinioConstants;
import org.example.backend.services.ModeratorService;
import org.example.backend.services.utilServices.EntityUpdateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ModeratorServiceImpl implements ModeratorService {
    private final ModeratorRepository moderatorRepository;
    private final CustomerRepository customerRepository;

    private final MinioClient minioClient;
    private static final String BUCKET_NAME = "profile-photo";

    @Override
    public void toggleBlockedStatus(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " not found"));

        customer.setBlocked(!customer.isBlocked());
        customerRepository.save(customer);
    }


    @Override
    public Moderator updateProfile(UpdateProfileRequest request) {
        Optional<Moderator> optionalModerator = moderatorRepository.findById(request.getId());
        if (optionalModerator.isPresent()) {
            Moderator existingModerator = optionalModerator.get();
            EntityUpdateUtil.updateField(existingModerator::setEmail, request.getEmail());

            if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
                String photoUrl = uploadPhotoToMinio(request.getProfilePicture());
                EntityUpdateUtil.updateField(existingModerator::setPhotoUrl, photoUrl);
            }
            existingModerator.setUpdatedDate(new Date());
            return moderatorRepository.save(existingModerator);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND");
        }

    }

    public String uploadPhotoToMinio(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return MinioConstants.BASE_URL_PROFILE_PHOTO + fileName;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage(), e);
        }
    }

}
