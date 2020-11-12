package com.example.uploadingfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.uploadingfiles.dao.DBStore;
import com.example.uploadingfiles.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uploadingfiles.storage.StorageService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Controller
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	DBStore dbstore;

	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	Validator validator = factory.getValidator();

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		return "uploadForm";
	}

/* This method will get the the uploaded file from Request and validate it */
	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {

		InputStream inputStream = file.getInputStream();
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

		String line;
		int currentLine = 0;
		List<User> userList = new ArrayList<>();

		while ((line = bufferedReader.readLine()) != null) {
			currentLine ++;

			String[] props =line.split(" ");

			if(props.length!=3){
				System.out.println("Input file should have three properties");
				break;
			}

			User user = new User();
			user.setUserNameId(props[0]);
			user.setNumberOfCoins(props[1]);
			user.setUserName(props[2]);
			validator.validate(user);
			Set<ConstraintViolation<User>> violations = validator.validate(user);
			violations.forEach(System.out::println);

			if(!violations.isEmpty()){
				System.out.println(currentLine);
				redirectAttributes.addFlashAttribute("errorLineNumber", currentLine);
				break;
			}
			userList.add(user);
			dbstore.save(user);
		}
		bufferedReader.close();

		storageService.store(userList);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + " and saved in database!");

		return "redirect:/";
	}

}
