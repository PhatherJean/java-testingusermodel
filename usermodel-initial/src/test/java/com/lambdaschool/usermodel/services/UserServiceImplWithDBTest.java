package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.HelperFunctions;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplWithDBTest
{
    @Autowired
    private UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void B_findUserId()
    {
        assertEquals("admin",
                userService.findUserById(4)
        .getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void BA_findUserByIdNotFound()
    {
        assertEquals("admin",
                userService.findUserById(10)
        .getUsername());
    }

    @Test
    public void C_findAll()
    {
        assertEquals(5,
                userService.findAll().size());
    }

    @Test
    public void D_delete()
    {
        userService.delete(13);
        assertEquals(4,
                userService.findAll().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void DA_notFoundDelete()
    {
        userService.delete(100);
        assertEquals(4,
                userService.findByName("admin")
        .getUsername());
    }

    @Test
    public void E_findByUsername()
    {
        assertEquals("admin",
                userService.findByName("admin").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
            public void AA_findByUsernameNotfound()
    {
        assertEquals("admin",
                userService.findByName("peanut").getUsername());
    }

    @Test
    public void AB_findByNameContaining()
    {
        assertEquals(4,
       userService.findByNameContaining("a")
        .size());
    }

    @Test
    public void F_save()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("panther",
                "ILuvBourbon!",
                "liqupr@live.com");
        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getUseremails()
                .add(new Useremail(u2, "liquor@liquor.local"));

        User saveU2 = userService.save(u2);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("liquor@liquor.local",
                saveU2.getUseremails()
        .get(0)
        .getUseremail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void FB_saveputnotfound()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("tiger",
                "IAmSleepy",
                "tiger@school.lambda");
        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "tiger@tiger.local"));
        u2.setUserid(777);

        User saveU2 = userService.save(u2);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("tiger@tiger.local",
                saveU2.getUseremails()
        .get(0).getUseremail());
    }

    @Test
    public void FA_saveputfound()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("mojo",
                "IAmSleepy",
                "mojo@school.lambda");
        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "mojo@corgi.local"));
        u2.setUserid(777);

        User saveU2 = userService.save(u2);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("mojo@corgi.com",
                saveU2.getUseremails()
                        .get(0).getUseremail());

    }

    @Test
    public void G_update()
    {
        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
                .thenReturn(true);

        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("Purp",
                "pitbull",
                "pitbull@school.lambda");
        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@tiger.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@ymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@gmail.local"));

        User saveU2 = userService.update(u2, 7);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("pitbull@tiger.local",
                saveU2.getUseremails()
                        .get(0).getUseremail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void GB_updateNotCurrentUserNorAdmin()
    {

        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("Purp",
                "pitbull",
                "pitbull@school.lambda");
        u2.getRoles()
                .add(new UserRoles(u2, r2));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@tiger.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@ymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "pitbull@gmail.local"));

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
                .thenReturn(false);
        User saveU2 = userService.update(u2, 8);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("pitbull@school.local",
                saveU2.getUseremails()
                        .get(0).getUseremail());
    }
}
