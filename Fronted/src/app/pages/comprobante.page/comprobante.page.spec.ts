import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ComprobantePage } from './comprobante.page';

describe('ComprobantePage', () => {
  let component: ComprobantePage;
  let fixture: ComponentFixture<ComprobantePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComprobantePage]
    }).compileComponents();

    fixture = TestBed.createComponent(ComprobantePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });
});
