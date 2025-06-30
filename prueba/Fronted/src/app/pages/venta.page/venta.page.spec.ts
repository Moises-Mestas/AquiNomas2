import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VentaPage } from './venta.page';

describe('VentaPage', () => {
  let component: VentaPage;
  let fixture: ComponentFixture<VentaPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VentaPage]
    })
      .compileComponents();

    fixture = TestBed.createComponent(VentaPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
