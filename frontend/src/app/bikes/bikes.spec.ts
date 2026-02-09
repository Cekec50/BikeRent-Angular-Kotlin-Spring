import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Bikes } from './bikes';

describe('Bikes', () => {
  let component: Bikes;
  let fixture: ComponentFixture<Bikes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Bikes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Bikes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
