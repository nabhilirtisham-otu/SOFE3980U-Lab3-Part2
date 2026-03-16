package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
@WebMvcTest(BinaryAPIController.class)
public class BinaryAPIControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void add() throws Exception {
        this.mvc.perform(get("/add").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("10001"));
    }
	@Test
    public void add2() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(111))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1010))
			.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10001))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("add"));
    }

    //Test to add only 0s
    //should be treated as 0 + 0 = 0
    @Test
    public void addZeros() throws Exception {
        this.mvc.perform(get("/add")
                .param("operand1", "0")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    //Test to add operands of different lengths
    //should still function normally
    @Test
    public void addUnequalLengths() throws Exception {
        this.mvc.perform(get("/add")
                .param("operand1", "1")
                .param("operand2", "1111"))
            .andExpect(status().isOk())
            .andExpect(content().string("10000"));
    }

    //Test to handle addition with leading zeroes
    //should still function normally
    @Test
    public void addWithLeadingZeros() throws Exception {
        this.mvc.perform(get("/add")
                .param("operand1", "00010")
                .param("operand2", "00001"))
            .andExpect(status().isOk())
            .andExpect(content().string("11"));
    }

    /*multiplication tests */

    //standard multiplication
    @Test
    public void multiplyNormal() throws Exception {
        mvc.perform(get("/multiply")
                .param("operand1", "11")
                .param("operand2", "10"))
            .andExpect(status().isOk())
            .andExpect(content().string("110"));
    }

    //multiplication by 0
    @Test
    public void multiplyByZero() throws Exception {
        mvc.perform(get("/multiply")
                .param("operand1", "101")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    /*Logical AND tests */

    //some overlapping digits (i.e. 1 AND 1, 0 AND 1)
    @Test
    public void andOverlap() throws Exception {
        mvc.perform(get("/and")
                .param("operand1", "1101")
                .param("operand2", "1011"))
            .andExpect(status().isOk())
            .andExpect(content().string("1001"));
    }

    //no overlapping digits (i.e. only 1 AND 0)
    @Test
    public void andNoOverlap() throws Exception {
        mvc.perform(get("/and")
                .param("operand1", "1000")
                .param("operand2", "0111"))
            .andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    /* Logical OR tests */

    @Test
    public void orOverlap() throws Exception {
        mvc.perform(get("/or")
                .param("operand1", "1100")
                .param("operand2", "0011"))
            .andExpect(status().isOk())
            .andExpect(content().string("1111"));
    }

    //OR with just 0
    @Test
    public void orWithZero() throws Exception {
        mvc.perform(get("/or")
                .param("operand1", "10101")
                .param("operand2", "0"))
            .andExpect(status().isOk())
            .andExpect(content().string("10101"));
    }

    /*JSON validation test */

    //check correct JSON generated
    @Test
    public void multiplyJson() throws Exception {
        mvc.perform(get("/multiply_json")
                .param("operand1", "101")
                .param("operand2", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operand1").value("101"))
            .andExpect(jsonPath("$.operand2").value("10"))
            .andExpect(jsonPath("$.operator").value("multiply"))
            .andExpect(jsonPath("$.result").value("1010"));
    }
}
