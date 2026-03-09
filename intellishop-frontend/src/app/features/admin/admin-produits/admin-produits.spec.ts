import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProduits } from './admin-produits';

describe('AdminProduits', () => {
  let component: AdminProduits;
  let fixture: ComponentFixture<AdminProduits>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminProduits]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProduits);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
