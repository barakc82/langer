import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCorpusStatisticsSentencesComponent } from './admin-corpus-statistics-sentences.component';

describe('AdminCorpusStatisticsSentencesComponent', () => {
  let component: AdminCorpusStatisticsSentencesComponent;
  let fixture: ComponentFixture<AdminCorpusStatisticsSentencesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminCorpusStatisticsSentencesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminCorpusStatisticsSentencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
