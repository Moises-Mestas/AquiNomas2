import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app.routes';  // Las rutas de tu aplicación

@NgModule({
  declarations: [
    // No es necesario agregar el componente standalone aquí
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,  // Rutas de la aplicación
  ],
  providers: [],
  // No se usa `bootstrap` porque el componente standalone se inicializa con `bootstrapApplication`
})
export class AppModule { }
