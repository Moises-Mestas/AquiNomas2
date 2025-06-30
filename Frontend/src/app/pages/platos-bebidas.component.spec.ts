import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlatosBebidasComponent } from './platos-bebidas.component';

describe('PlatosBebidasComponent', () => {
  let component: PlatosBebidasComponent;
  let fixture: ComponentFixture<PlatosBebidasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatosBebidasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlatosBebidasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
