import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PromocionPage } from './promocion.page';

describe('PromocionPage', () => {
  let component: PromocionPage;
  let fixture: ComponentFixture<PromocionPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromocionPage]
    }).compileComponents();

    fixture = TestBed.createComponent(PromocionPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });
});
