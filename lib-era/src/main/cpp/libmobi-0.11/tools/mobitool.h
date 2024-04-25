
#ifndef mobitool_h
#define mobitool_h

void print_meta(const MOBIData *m);

int create_epub(const MOBIRawml *rawml, const char *fullpath);

int dump_cover(const MOBIData *m, const char *fullpath);

#endif