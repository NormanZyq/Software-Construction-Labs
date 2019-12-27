package application.AtomStructure;

import MyException.AtomStructureException.ElectronNotExistException;
import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import abs.ConcreteCircularOrbit;
import abs.Position;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * this class represents an atom structure.
 */
public class AtomStructure extends ConcreteCircularOrbit<Nuclear, Electron> {
    /*
        AF
        this class represents an atom structure,
        including kernel, tracks, electrons.

        RI
        true

        safety from exposure
        this class is inherited from ConcreteCircularOrbit,
        no other observers are added.
        the only two 3 mutators are under control.
    */

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(AtomStructure.class);

    /**
     * create an empty atom structure orbit model.
     *
     * @return an empty atom structure orbit model
     */
    public static AtomStructure empty() {
        return new AtomStructure();
    }

    /**
     * remove an electron from the passed in level of track.
     *
     * @param level level of a track which you want to remove an electron
     * @throws NoSuchLevelOfTrackException        throws when the level
     *                                            is not in the orbit model
     * @throws ElectronNotExistException throws when there's no more
     *                                            electron on the level of track
     */
    public void removeElectronFromTrack(final int level)
            throws NoSuchLevelOfTrackException,
            ElectronNotExistException {
        if (tracks.get(level) == null) {
            // track does not exist
            throw new NoSuchLevelOfTrackException(level + "号轨道不存在。");
        }
        List<Electron> electrons = getObjectsByLevel(level);
        if (electrons.size() > 0) {
            try {
                removeObject(electrons.get(0));
            } catch (ObjectNotExistException ignored) {
            }
        } else {
            throw new ElectronNotExistException();
        }
        checkRep();
    }

    /**
     * check RI.
     * in this class, always assert true
     */
    private void checkRep() {
        assert true;
    }

    /**
     * transit an electron to @code toLevel.
     *
     * @param e       @requires must be added to the orbit before
     * @param toLevel new level
     * @throws ObjectNotExistException throws when
     *                                     the electron is not found
     */
    private void electronTransit(final Electron e, final int toLevel)
            throws ObjectNotExistException {
        double oldAngle = getPosition(e).getAngle();
        changePosition(e, new Position(toLevel, toLevel, oldAngle));
    }

    /**
     * transit @code num of electrons from fromLevel to toLevel.
     * if the number of electrons on fromLevel
     * is not enough, then transit them all
     *
     * @param fromLevel from which level to transit
     * @param toLevel   to which level
     * @param num       number of electrons
     * @throws NoSuchLevelOfTrackException throws then the
     *                                     fromLevel or toLevel is not exist
     */
    public void electronTransit(final int fromLevel,
                                final int toLevel,
                                final int num)
            throws NoSuchLevelOfTrackException {
        if (!containsLevel(fromLevel)) {
            throw new NoSuchLevelOfTrackException(fromLevel + "层轨道不存在");
        } else if (!containsLevel(toLevel)) {
            throw new NoSuchLevelOfTrackException(toLevel + "层轨道不存在");
        } else {
            List<Electron> electronOnFromLevel =
                    objectsOnTrack.get(fromLevel);

            int size = electronOnFromLevel.size();
            int loop = Integer.min(size, num);

            try {
                for (int i = 0; i < loop; i++) {
                    electronTransit(electronOnFromLevel.get(0), toLevel);
                }
            } catch (ObjectNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeBackWithPrintWriter() throws IOException {
        long time1 = System.currentTimeMillis();
        // new file
        PrintWriter p = new PrintWriter("src/txt/New AtomStructure_PrintWriter.txt");

        // write file
        // 1. write name
        p.println(String.format("%s ::= %s", "ElementName", getCenterObject().getName()));
        // 2. number of track
        p.println(String.format("%s ::= %d", "NumberOfTracks", trackCount()));
        // 3. write details
        p.print("NumberOfElectron ::= ");
        StringBuilder sb = new StringBuilder();
        for (int i = 1, size = trackCount(); i <= size; i++) {
            sb.append(String.format("%d/%d;", i, getObjectsByLevel(i).size()));
        }
        sb.deleteCharAt(sb.length() - 1);
        p.print(sb.toString());

        long time2 = System.currentTimeMillis();
        log.info("准备数据+使用PrintWriter写出文件AtomStructure共花费时间：" + (time2 - time1));

        p.flush();
        p.close();
    }

    @Override
    public void writeBackWithStream() throws IOException {
        long time1 = System.currentTimeMillis();
        FileOutputStream stream = new FileOutputStream("src/txt/New AtomStructure_Stream.txt");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s ::= %s\n", "ElementName", getCenterObject().getName())).
                append(String.format("%s ::= %d\n", "NumberOfTracks", trackCount())).
                append("NumberOfElectron ::= ");
        for (int i = 1, size = trackCount(); i <= size; i++) {
            sb.append(String.format("%d/%d;", i, getObjectsByLevel(i).size()));
        }
        sb.deleteCharAt(sb.length() - 1);

        long time2 = System.currentTimeMillis();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();

        log.info("准备数据+写出文件AtomStructure共花费时间：" + (time3 - time1));
        log.info("使用Stream写出文件AtomStructure共花费时间：" + (time3 - time2));

        stream.flush();
        stream.close();
    }

    @Override
    public void writeBackWithBuffer() throws IOException {
        long time1 = System.currentTimeMillis();

        OutputStream output = new FileOutputStream("src/txt/New AtomStructure_Buffer.txt");
        BufferedOutputStream stream = new BufferedOutputStream(output);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s ::= %s\n", "ElementName", getCenterObject().getName())).
                append(String.format("%s ::= %d\n", "NumberOfTracks", trackCount())).
                append("NumberOfElectron ::= ");
        for (int i = 1, size = trackCount(); i <= size; i++) {
            sb.append(String.format("%d/%d;", i, getObjectsByLevel(i).size()));
        }
        sb.deleteCharAt(sb.length() - 1);

        long time2 = System.currentTimeMillis();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();

        log.info("准备数据+写出文件AtomStructure共花费时间：" + (time3 - time1));
        log.info("使用Buffer写出文件AtomStructure共花费时间：" + (time3 - time2));

        output.flush();
        stream.flush();
        stream.close();
        output.close();
    }
}
