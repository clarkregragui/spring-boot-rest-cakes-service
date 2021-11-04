package com.riggy.example.cakes.model.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CakeDTO implements Serializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -9089461472324909474L;
	
    @NotBlank(message = "A title is mandatory")
    private String title;
    @NotBlank(message = "A description is mandatory")
    private String desc;
    @NotBlank(message = "An image is mandatory")
    private String image;
    @NotEmpty(message = "Ingredients list cannot be empty.")
    private List<String> ingredients;
    
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=4, fraction=2)
    private BigDecimal price;
}
