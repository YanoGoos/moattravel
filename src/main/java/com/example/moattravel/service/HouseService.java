package com.example.moattravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.moattravel.entity.House;
import com.example.moattravel.form.HouseRegisterForm;
import com.example.moattravel.repository.HouseRepository;

@Service
public class HouseService {
	private final HouseRepository houseRepository;
	
	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(HouseRegisterForm houseResisterForm) {
		House house = new House();
		MultipartFile imageFile = houseResisterForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashdImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashdImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashdImageName);
		}
		
		house.setName(houseResisterForm.getName());
		house.setDescription(houseResisterForm.getDescription());
		house.setPrice(houseResisterForm.getPrice());
		house.setCapacity(houseResisterForm.getCapacity());
		house.setPostalCode(houseResisterForm.getPostalCode());
		house.setAddress(houseResisterForm.getAddress());
		house.setPhoneNumber(houseResisterForm.getPhoneNumber());
		
		houseRepository.save(house);
	}
	
	//UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for(int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashdFileName = String.join(".", fileNames);
		return hashdFileName;
	}
	
	//画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

