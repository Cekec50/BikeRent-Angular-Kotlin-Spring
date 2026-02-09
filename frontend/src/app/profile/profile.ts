import { Component, OnInit } from '@angular/core';
import { User } from '../model/UserModel';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {

  
  user!:User;

  ngOnInit(): void {
    this.user = this.getUserData();
  }

  getUserData(): User{
    // TODO: Will get data from DB
    return {
              id: 1,
              username: "nidzoooo",
              password: "",
              firstname: "Nikola",
              lastname: "Nikolic",
              phone: "+318124411",
              email: "nikola@gmail.com",
              isAdmin: false
            };
  }


}
