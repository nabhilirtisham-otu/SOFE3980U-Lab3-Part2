package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.runner.RunWith;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

import static org.hamcrest.Matchers.containsString;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void getDefault() throws Exception {
        this.mvc.perform(get("/"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", ""))
			.andExpect(model().attribute("operand1Focused", false));
    }
	
	    @Test
    public void getParameter() throws Exception {
        this.mvc.perform(get("/").param("operand1","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", "111"))
			.andExpect(model().attribute("operand1Focused", true));
    }
	@Test
	    public void postParameter() throws Exception {
        this.mvc.perform(post("/").param("operand1","111").param("operator","+").param("operand2","111"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
			.andExpect(model().attribute("result", "1110"))
			.andExpect(model().attribute("operand1", "111"));
    }

    //Test for invalid operators
    //should show error view
    @Test
    public void postInvalidOperator() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "%")
                .param("operand2", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("Error"));
    }

    //Test for empty operands
    //should be treated as 0 + 0 = 0
    @Test
    public void postEmptyOperands() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "")
                .param("operator", "+")
                .param("operand2", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    //Test for leading zeros
    //should be ignored
    @Test
    public void postLeadingZeros() throws Exception {
        this.mvc.perform(post("/")
                .param("operand1", "00101")
                .param("operator", "+")
                .param("operand2", "00011"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1000"));
    }

    /* Multiplication tests */

    //normal multiplication - 5 x 2
    @Test
    public void postMultiplyNormal() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "*")
                .param("operand2", "10"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1010"));
    }

    //multiplication by 0 - 11 x 0
    @Test
    public void postMultiplyByZero() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "1011")
                .param("operator", "*")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    //multiplication by 1 - 12 x 1
    @Test
    public void postMultiplyByOne() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "1101")
                .param("operator", "*")
                .param("operand2", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1101"));
    }

    /* Logical AND tests */

    //some overlapping digits (i.e. 1 AND 1, 0 AND 1)
    @Test
    public void postAndOverlap() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "1101")
                .param("operator", "&")
                .param("operand2", "1011"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1001"));
    }

    //no overlapping digits (i.e. only 1 AND 0)
    @Test
    public void postAndNoOverlap() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "1000")
                .param("operator", "&")
                .param("operand2", "0111"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    /* Logical OR tests */

    //some overlapping digits (i.e. 1 OR 1, 0 OR 1, 0 OR 0)
    @Test
    public void postOrOverlap() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "1100")
                .param("operator", "|")
                .param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1110"));
    }

    //OR with just 0
    @Test
    public void postOrWithZero() throws Exception {
        mvc.perform(post("/")
                .param("operand1", "10101")
                .param("operator", "|")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "10101"));
    }
}