import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCorpusStatisticsWords } from './admin-corpus-statistics-words.component';

describe('AdminCorpusStatisticsWords', () => {
  let component: AdminCorpusStatisticsWords;
  let fixture: ComponentFixture<AdminCorpusStatisticsWords>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminCorpusStatisticsWords ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminCorpusStatisticsWords);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
