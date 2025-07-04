import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecetaPage } from './receta.page';

describe('RecetaPage', () => {
  let component: RecetaPage;
  let fixture: ComponentFixture<RecetaPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecetaPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecetaPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
