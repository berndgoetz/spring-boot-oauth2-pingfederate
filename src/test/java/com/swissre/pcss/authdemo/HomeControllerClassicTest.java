package com.swissre.pcss.authdemo;

import com.swissre.pcss.authdemo.controller.MainController;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MainController.class)
public class HomeControllerClassicTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Ignore
    @WithMockUser
    public void verifiesHomePageLoads() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
               .andExpect(MockMvcResultMatchers.model().hasNoErrors())
//               .andExpect(MockMvcResultMatchers.view().name("index"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

}