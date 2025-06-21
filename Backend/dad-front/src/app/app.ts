import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
  imports: [
    RouterOutlet,
    FormsModule
  ]
})
export class App implements OnInit {
  protected title: string = 'dad-front';

  constructor() {}

  ngOnInit(): void {
    // Aquí puedes colocar cualquier inicialización si lo necesitas
    console.log('Aplicación cargada');
  }
}
