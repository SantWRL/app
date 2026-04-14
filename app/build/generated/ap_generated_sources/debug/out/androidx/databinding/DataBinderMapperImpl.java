package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new br.ufpi.lgpd.educacional.DataBinderMapperImpl());
  }
}
