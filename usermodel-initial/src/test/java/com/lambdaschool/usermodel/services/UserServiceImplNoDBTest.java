package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
properties = {
        "command.line.runner.enable=false"
})
public class UserServiceImplNoDBTest
{

    @Autowired
    private UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    @MockBean
    private UserRepository userrepos;

    @MockBean
    private RoleService roleService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);

        User u1 = new User("admin",
                "ILuvM4th!",
                "admin@lambdaschool.test");
        u1.getRoles()
                .add(new UserRoles(u1,r1));
        u1.getRoles()
                .add(new UserRoles(u1,r2));
        u1.getRoles()
                .add(new UserRoles(u1,r3));

        u1.getUseremails()
                .add(new Useremail(u1, "admin@lmail.test"));
        u1.getUseremails()
                .get(0)
                .setUseremailid(10);
        u1.getUseremails()
                .add(new Useremail(u1, "admin@ymail.com"));
        u1.getUseremails()
                .get(1)
                .setUseremailid(11);
        u1.setUserid(101);
        userList.add(u1);

        ArrayList<UserRoles> datas = new ArrayList<>();

        User u2 = new User("cinnan",
                "ILuvM4th!",
                "admin@lambdaschool.test");
        u2.getRoles()
                .add(new UserRoles(u2,r1));
        u2.getRoles()
                .add(new UserRoles(u2,r2));
        u2.getRoles()
                .add(new UserRoles(u2,r3));

        u2.getUseremails()
                .add(new Useremail(u2, "admin@lmail.test"));
        u2.getUseremails()
                .get(0)
                .setUseremailid(20);
        u2.getUseremails()
                .add(new Useremail(u2, "admin@ymail.com"));
        u2.getUseremails()
                .get(1)
                .setUseremailid(22);
        u2.setUserid(102);
        userList.add(u2);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void B_findUserId()
    {
        Mockito.when(userrepos.findById(101L))
                .thenReturn(Optional.of(userList.get(0)));
        assertEquals("admin",
                userService.findUserById(101L)
                        .getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void BA_findUserByIdNotFound()
    {
        Mockito.when(userrepos.findById(10L))
                .thenReturn(Optional.empty());
        assertEquals("admin",
                userService.findUserById(10L)
                        .getUsername());
    }

    @Test
    public void C_findAll()
    {
        Mockito.when(userrepos.findAll())
                .thenReturn(userList);
        assertEquals(5,
                userService.findAll()
                        .size());
    }

    @Test
    public void D_delete()
    {
        Mockito.when(userrepos.findById(103L))
                .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
                .when(userrepos)
                .deleteById(103L);

        userService.delete(103L);
        assertEquals(5,
                userList.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void DA_notFoundDelete()
    {
        Mockito.when(userrepos.findById(10L))
                .thenReturn(Optional.empty());

        Mockito.doNothing()
                .when(userrepos)
                .deleteById(10L);

        userService.delete(100);
        assertEquals(4,
                userList.size());
    }

    @Test
    public void E_findByUsername()
    {
        Mockito.when(userrepos.findByUsername("admin"))
                .thenReturn(userList.get(0));

        assertEquals("admin",
                userService.findByName("admin").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void AA_findByUsernameNotfound()
    {
        Mockito.when(userrepos.findByUsername("nonsense"))
                .thenReturn(null);

        assertEquals("admin",
                userService.findByName("nonsense").getUsername());
    }

    @Test
    public void AB_findByNameContaining()
    {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("a"))
                .thenReturn(userList);

        assertEquals(5,
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

        Mockito.when(roleService.findRoleById(2))
                .thenReturn(r2);

        Mockito.when(userrepos.findById(103L))
                .thenReturn(Optional.of(userList.get(2)));

        Mockito.when(userrepos.save(any(User.class)))
                .thenReturn(u2);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
                .thenReturn(true);

        assertEquals("bunny@email.thump",
                userService.update(u2, 103L)
                .getUseremails()
                .get(2)
                .getUseremail());
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

        Mockito.when(roleService.findRoleById(2))
                .thenReturn(r2);

        Mockito.when(userrepos.findById(103L))
                .thenReturn(Optional.empty());

        Mockito.when(userrepos.save(any(User.class)))
                .thenReturn(u2);

        User saveU2 = userService.update(u2, 8);

        System.out.println("***DATA***");
        System.out.println("\n"+ saveU2+"\n");
        System.out.println("***DATA***");

        assertEquals("pitbull@school.local",
                userService.update(u2, 103L)
                        .getUseremails()
                        .get(2)
                        .getUseremail());
    }
    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
                .when(userrepos)
                .deleteAll();

        userService.deleteAll();
        assertEquals(5, userList.size());
    }
}
